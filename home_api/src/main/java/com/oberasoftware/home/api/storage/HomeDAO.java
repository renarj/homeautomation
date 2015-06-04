package com.oberasoftware.home.api.storage;

import com.oberasoftware.home.api.storage.model.Container;
import com.oberasoftware.home.api.storage.model.ControllerItem;
import com.oberasoftware.home.api.storage.model.DeviceItem;
import com.oberasoftware.home.api.storage.model.Item;
import com.oberasoftware.home.api.storage.model.PluginItem;
import com.oberasoftware.home.api.storage.model.UIItem;

import java.util.List;
import java.util.Optional;

/**
 * @author renarj
 */
public interface HomeDAO {
    <T extends Item> Optional<T> findItem(String id);

    Optional<ControllerItem> findController(String controllerId);

    List<ControllerItem> findControllers();

    <T extends Container> Optional<T> findContainer(String id);

    List<Container> findRootContainers();

    List<Container> findContainers();

    List<Container> findContainers(String parentId);

    List<UIItem> findUIItems(String containerId);

    Optional<PluginItem> findPlugin(String controllerId, String pluginId);

    List<PluginItem> findPlugins(String controllerId);

    List<DeviceItem> findDevices(String controllerId, String pluginId);

    Optional<DeviceItem> findDevice(String controllerId, String pluginId, String deviceId);

    List<DeviceItem> findDevices();
}
