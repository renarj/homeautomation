package com.oberasoftware.home.api.managers;

import com.oberasoftware.home.api.exceptions.HomeAutomationException;
import com.oberasoftware.home.api.model.storage.ControllerItem;
import com.oberasoftware.home.api.model.storage.DeviceItem;
import com.oberasoftware.home.api.model.storage.Item;
import com.oberasoftware.home.api.model.storage.PluginItem;

import java.util.List;
import java.util.Map;

/**
 * @author renarj
 */
public interface ItemManager {
    ControllerItem createOrUpdateController(String controllerId) throws HomeAutomationException;

    PluginItem createOrUpdatePlugin(String controllerId, String pluginId, String name, Map<String, String> properties) throws HomeAutomationException;

    DeviceItem createOrUpdateDevice(String controllerId, String pluginId, String deviceId, String name, Map<String, String> properties) throws HomeAutomationException;

    List<ControllerItem> findControllers();

    List<PluginItem> findPlugins(String controllerId);

    List<DeviceItem> findDevices(String controllerId, String pluginId);

    List<DeviceItem> findDevices(String controllerId);

    Item findItem(String id);
}
