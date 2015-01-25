package com.oberasoftware.home.service;

import com.oberasoftware.home.api.AutomationBus;
import com.oberasoftware.home.api.events.EventHandler;
import com.oberasoftware.home.api.exceptions.DataStoreException;
import com.oberasoftware.home.api.managers.DeviceManager;
import com.oberasoftware.home.api.model.Device;
import com.oberasoftware.home.api.storage.CentralDatastore;
import com.oberasoftware.home.api.storage.model.DeviceItem;
import com.oberasoftware.home.api.storage.model.PluginItem;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author renarj
 */
@Component
public class DeviceManagerImpl implements DeviceManager, EventHandler {
    private static final Logger LOG = getLogger(DeviceManagerImpl.class);

    @Autowired
    private CentralDatastore centralDatastore;

    @Autowired
    private AutomationBus automationBus;

    @Override
    public DeviceItem registerDevice(String pluginId, Device device) throws DataStoreException {
        LOG.debug("Registering device: {} for plugin: {}", device, pluginId);
        String controllerId = automationBus.getControllerId();

        Optional<PluginItem> plugin = centralDatastore.findPlugin(controllerId, pluginId);

        centralDatastore.beginTransaction();
        try {
            Optional<DeviceItem> deviceItem = centralDatastore.findDevice(controllerId, plugin.get().getPluginId(), device.getId());
            String id = generateId();
            if(deviceItem.isPresent()) {
                LOG.debug("Device: {} already exist updating device with id: {}", device, deviceItem.get().getId());
                id = deviceItem.get().getId();
            } else {
                LOG.debug("Device: {} does not yet exist, creating new with id: {}", device, id);
            }

            return centralDatastore.store(new DeviceItem(id, controllerId, plugin.get().getPluginId(), device.getId(),
                    device.getName(), device.getProperties(), new HashMap<>()));
        } finally {
            centralDatastore.commitTransaction();
        }
    }

    @Override
    public Optional<DeviceItem> findDeviceItem(String controllerId, String pluginId, String deviceId) {
        return centralDatastore.findDevice(controllerId, pluginId, deviceId);
    }

    private String generateId() {
        return UUID.randomUUID().toString();
    }

    @Override
    public List<DeviceItem> getDevices() {
        return centralDatastore.findDevices();
    }

}
