package com.oberasoftware.home.api.managers;

import com.oberasoftware.home.api.exceptions.HomeAutomationException;
import com.oberasoftware.home.api.storage.model.DeviceItem;
import com.oberasoftware.home.api.storage.model.PluginItem;

import java.util.Map;

/**
 * @author renarj
 */
public interface ItemManager {
    PluginItem createOrUpdatePlugin(String controllerId, String pluginId, String name, Map<String, String> properties) throws HomeAutomationException;

    DeviceItem createOrUpdateDevice(String controllerId, String pluginId, String deviceId, String name, Map<String, String> properties) throws HomeAutomationException;
}
