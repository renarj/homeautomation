package com.oberasoftware.home.service;

import com.oberasoftware.home.api.AutomationBus;
import com.oberasoftware.home.api.events.EventHandler;
import com.oberasoftware.home.api.events.EventSubscribe;
import com.oberasoftware.home.api.events.devices.DeviceInformationEvent;
import com.oberasoftware.home.api.exceptions.DataStoreException;
import com.oberasoftware.home.api.managers.DeviceManager;
import com.oberasoftware.home.api.model.Device;
import com.oberasoftware.home.api.storage.CentralDatastore;
import com.oberasoftware.home.api.storage.model.DeviceItem;
import com.oberasoftware.home.api.storage.model.DevicePlugin;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author renarj
 */
@Component
public class DeviceManagerImpl implements DeviceManager, EventHandler {
    private static final Logger LOG = getLogger(DeviceManagerImpl.class);

    private Lock lock = new ReentrantLock();

    @Autowired
    private CentralDatastore centralDatastore;

    @Autowired
    private AutomationBus automationBus;

    @Override
    public DeviceItem registerDevice(String pluginId, Device device) throws DataStoreException {
        LOG.debug("Registering device: {} for plugin: {}", device, pluginId);
        String controllerId = automationBus.getControllerId();

        Optional<DevicePlugin> plugin = centralDatastore.findPlugin(controllerId, pluginId);

        lock.lock();
        try {
            Optional<DeviceItem> deviceItem = centralDatastore.findDevice(controllerId, plugin.get().getPluginId(), device.getId());
            String id = generateId();
            if(deviceItem.isPresent()) {
                LOG.debug("Device: {} already exist updating device with id: {}", device, deviceItem.get().getId());
                id = deviceItem.get().getId();
            } else {
                LOG.debug("Device: {} does not yet exist, creating new with id: {}", device, id);
            }

            return centralDatastore.store(new DeviceItem(id, controllerId, plugin.get().getPluginId(), device.getId(), device.getName(), device.getProperties()));
        } finally {
            lock.unlock();
        }
    }

    private String generateId() {
        return UUID.randomUUID().toString();
    }

    @Override
    public List<DeviceItem> getDevices() {
        return null;
    }

    @EventSubscribe
    public void receive(DeviceInformationEvent event) throws Exception {
        LOG.debug("Received a device update for plugin: {} and device: {}", event.getPluginId(), event.getDevice());
        registerDevice(event.getPluginId(), event.getDevice());
    }
}
