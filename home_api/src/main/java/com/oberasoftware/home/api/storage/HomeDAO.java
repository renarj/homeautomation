package com.oberasoftware.home.api.storage;

import com.oberasoftware.home.api.model.storage.Container;
import com.oberasoftware.home.api.model.storage.ControllerItem;
import com.oberasoftware.home.api.model.storage.DeviceItem;
import com.oberasoftware.home.api.model.storage.Item;
import com.oberasoftware.home.api.model.storage.PluginItem;
import com.oberasoftware.home.api.model.storage.RuleItem;
import com.oberasoftware.home.api.model.storage.UIItem;
import com.oberasoftware.home.api.model.storage.VirtualItem;

import java.util.List;
import java.util.Optional;

/**
 * @author renarj
 */
public interface HomeDAO {
    <T extends Item> Optional<T> findItem(Class<T> type, String id);

    Optional<ControllerItem> findController(String controllerId);

    List<ControllerItem> findControllers();

    Optional<Container> findContainer(String id);

    List<Container> findRootContainers();

    List<Container> findContainers();

    List<Container> findContainers(String parentId);

    List<UIItem> findUIItems(String containerId);

    Optional<PluginItem> findPlugin(String controllerId, String pluginId);

    List<PluginItem> findPlugins(String controllerId);

    List<DeviceItem> findDevices(String controllerId, String pluginId);

    Optional<DeviceItem> findDevice(String controllerId, String pluginId, String deviceId);

    List<DeviceItem> findDevices();

    <T extends VirtualItem> List<T> findVirtualItems(Class<T> type);

    <T extends VirtualItem> List<T> findVirtualItems(Class<T> type, String controllerId);

    List<RuleItem> findRules();

    List<RuleItem> findRules(String controllerId);
}
