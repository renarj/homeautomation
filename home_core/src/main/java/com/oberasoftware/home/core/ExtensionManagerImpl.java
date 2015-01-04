package com.oberasoftware.home.core;

import com.oberasoftware.home.api.AutomationBus;
import com.oberasoftware.home.api.exceptions.DataStoreException;
import com.oberasoftware.home.api.exceptions.HomeAutomationException;
import com.oberasoftware.home.api.exceptions.RuntimeHomeAutomationException;
import com.oberasoftware.home.api.extensions.AutomationExtension;
import com.oberasoftware.home.api.extensions.DeviceExtension;
import com.oberasoftware.home.api.extensions.ExtensionManager;
import com.oberasoftware.home.api.managers.DeviceManager;
import com.oberasoftware.home.api.model.Device;
import com.oberasoftware.home.api.storage.CentralDatastore;
import com.oberasoftware.home.api.storage.model.ControllerItem;
import com.oberasoftware.home.api.storage.model.DeviceItem;
import com.oberasoftware.home.api.storage.model.DevicePlugin;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author renarj
 */
@Component
public class ExtensionManagerImpl implements ExtensionManager {
    private static final Logger LOG = getLogger(ExtensionManagerImpl.class);

    private List<AutomationExtension> extensions = new ArrayList<>();

    @Autowired
    private DeviceManager deviceManager;

    @Autowired
    private CentralDatastore centralDatastore;

    @Autowired
    private AutomationBus automationBus;

    @Override
    public List<AutomationExtension> getExtensions() {
        return extensions;
    }

    @Override
    public void registerController(String controllerId) throws DataStoreException {
        if(!centralDatastore.findController(controllerId).isPresent()) {
            LOG.debug("Initial startup, new controller detected registering in central datastore");
            centralDatastore.store(new ControllerItem(generateId(), controllerId));
        } else {
            LOG.debug("Controller: {} was already registered");
        }
    }

    @Override
    public void registerExtension(AutomationExtension extension) throws HomeAutomationException {
        LOG.info("Registering extension: {}", extension);

        if(extension instanceof DeviceExtension) {
            registerDeviceExtension((DeviceExtension)extension);
        }
    }

    private void registerDeviceExtension(DeviceExtension deviceExtension) throws HomeAutomationException {
        Optional<DevicePlugin> plugin = centralDatastore.findPlugin(automationBus.getControllerId(), deviceExtension.getId());
        if(!plugin.isPresent()) {
            LOG.debug("Plugin does not exist, storing in central datastore: {}", deviceExtension);
            Optional<ControllerItem> controllerItem = centralDatastore.findController(automationBus.getControllerId());
            String pluginId = generateId();

            centralDatastore.store(new DevicePlugin(pluginId, controllerItem.get().getControllerId(),
                    deviceExtension.getId(), deviceExtension.getName()));
            controllerItem.get().addPluginId(pluginId);
            centralDatastore.store(controllerItem.get());
        }

        List<Device> devices = deviceExtension.getDevices();
        devices.forEach(d -> {
            try {
                registerDevice(deviceExtension, d);
                plugin.get().addDevice(d.getId());
            } catch (DataStoreException e) {
                throw new RuntimeHomeAutomationException("Unable to store plugin devices", e);
            }
        });
    }

    private void registerDevice(DeviceExtension deviceExtension, Device device) throws DataStoreException {
        Optional<DevicePlugin> plugin = centralDatastore.findPlugin(automationBus.getControllerId(), deviceExtension.getId());

        Optional<DeviceItem> deviceItem = centralDatastore.findDevice(automationBus.getControllerId(), plugin.get().getPluginId(), device.getId());

        if(!deviceItem.isPresent()) {
            centralDatastore.store(new DeviceItem(generateId(), plugin.get().getPluginId(), device.getId(), device.getName(), device.getProperties()));
        }
    }

    private String generateId() {
        return UUID.randomUUID().toString();
    }
}
