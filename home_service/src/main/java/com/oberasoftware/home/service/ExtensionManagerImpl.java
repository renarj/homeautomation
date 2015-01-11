package com.oberasoftware.home.service;

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
import com.oberasoftware.home.api.storage.model.DevicePlugin;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author renarj
 */
@Component
public class ExtensionManagerImpl implements ExtensionManager {
    private static final Logger LOG = getLogger(ExtensionManagerImpl.class);

    private ConcurrentMap<String, AutomationExtension> extensions = new ConcurrentHashMap<>();

    @Autowired
    private DeviceManager deviceManager;

    @Autowired
    private CentralDatastore centralDatastore;

    @Autowired
    private AutomationBus automationBus;

    @Override
    public List<AutomationExtension> getExtensions() {
        return new ArrayList<>(extensions.values());
    }

    @Override
    public Optional<AutomationExtension> getExtension(String extensionId) {
        return Optional.ofNullable(extensions.get(extensionId));
    }

    @Override
    public void registerController(String controllerId) throws DataStoreException {
        if(!centralDatastore.findController(controllerId).isPresent()) {
            LOG.debug("Initial startup, new controller detected registering in central datastore");
            centralDatastore.store(new ControllerItem(generateId(), controllerId));
        } else {
            LOG.debug("Controller: {} was already registered", controllerId);
        }
    }

    @Override
    public void registerExtension(AutomationExtension extension) throws HomeAutomationException {
        extensions.putIfAbsent(extension.getId(), extension);

        LOG.info("Registering extension: {}", extension);

        if(extension instanceof DeviceExtension) {
            registerDeviceExtension((DeviceExtension) extension);
        }
    }

    private void registerDeviceExtension(DeviceExtension deviceExtension) throws HomeAutomationException {
        Optional<DevicePlugin> plugin = centralDatastore.findPlugin(automationBus.getControllerId(), deviceExtension.getId());
        if(!plugin.isPresent()) {
            LOG.debug("Plugin does not exist, storing in central datastore: {}", deviceExtension);
            Optional<ControllerItem> controllerItem = centralDatastore.findController(automationBus.getControllerId());
            String pluginId = generateId();

            centralDatastore.store(new DevicePlugin(pluginId, controllerItem.get().getControllerId(),
                    deviceExtension.getId(), deviceExtension.getName(), new HashMap<>()));
            controllerItem.get().addPluginId(pluginId);
            centralDatastore.store(controllerItem.get());
        }

        registerDevices(deviceExtension);
    }

    private void registerDevices(DeviceExtension deviceExtension) {
        Optional<DevicePlugin> plugin = centralDatastore.findPlugin(automationBus.getControllerId(), deviceExtension.getId());

        List<Device> devices = deviceExtension.getDevices();
        devices.forEach(d -> {
            try {
                deviceManager.registerDevice(deviceExtension.getId(), d);
                plugin.get().addDevice(d.getId());
            } catch (DataStoreException e) {
                throw new RuntimeHomeAutomationException("Unable to store plugin devices", e);
            }
        });


        try {
            centralDatastore.store(plugin.get());
        } catch (DataStoreException e) {
            LOG.error("", e);
        }
    }

    private String generateId() {
        return UUID.randomUUID().toString();
    }
}
