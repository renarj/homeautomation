package com.oberasoftware.home.service;

import com.oberasoftware.home.api.AutomationBus;
import com.oberasoftware.home.api.exceptions.HomeAutomationException;
import com.oberasoftware.home.api.managers.DeviceManager;
import com.oberasoftware.home.api.managers.ItemManager;
import com.oberasoftware.home.api.model.Device;
import com.oberasoftware.home.api.storage.HomeDAO;
import com.oberasoftware.home.api.storage.model.DeviceItem;
import com.oberasoftware.home.api.storage.model.PluginItem;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author renarj
 */
@Component
public class DeviceManagerImpl implements DeviceManager {
    private static final Logger LOG = getLogger(DeviceManagerImpl.class);

    @Autowired
    private HomeDAO homeDAO;

    @Autowired
    private ItemManager itemManager;

    @Autowired
    private AutomationBus automationBus;

    @Override
    public DeviceItem registerDevice(String pluginId, Device device) throws HomeAutomationException {
        LOG.debug("Registering device: {} for plugin: {}", device, pluginId);
        String controllerId = automationBus.getControllerId();

        Optional<PluginItem> plugin = homeDAO.findPlugin(controllerId, pluginId);
        if(plugin.isPresent()) {
            return itemManager.createOrUpdateDevice(automationBus.getControllerId(), plugin.get().getPluginId(), device.getId(), device.getName(), device.getProperties());
        } else {
            LOG.error("Unable to register device: {} for plugin: {}", device, pluginId);
            return null;
        }
    }

    @Override
    public Optional<DeviceItem> findDeviceItem(String controllerId, String pluginId, String deviceId) {
        return homeDAO.findDevice(controllerId, pluginId, deviceId);
    }

    @Override
    public List<DeviceItem> getDevices() {
        return homeDAO.findDevices();
    }

}
