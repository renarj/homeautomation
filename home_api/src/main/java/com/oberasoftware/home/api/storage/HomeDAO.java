package com.oberasoftware.home.api.storage;

import com.oberasoftware.home.api.model.storage.*;

import java.util.List;
import java.util.Optional;

/**
 * @author renarj
 */
public interface HomeDAO {
    <T extends HomeEntity> Optional<T> findItem(Class<T> type, String id);

    Optional<ControllerItem> findController(String controllerId);

    List<ControllerItem> findControllers();

    Optional<Container> findContainer(String id);

    List<Container> findDashboardContainers(String dashboardId);

    List<Container> findContainers();

    List<Container> findContainers(String parentId);

    List<Dashboard> findDashboards();

    List<Widget> findWidgets(String containerId);

    Optional<PluginItem> findPlugin(String controllerId, String pluginId);

    List<PluginItem> findPlugins(String controllerId);

    List<DeviceItem> findDevices(String controllerId, String pluginId);

    Optional<DeviceItem> findDevice(String controllerId, String pluginId, String deviceId);

    List<DeviceItem> findDevices(String controllerId);

    <T extends VirtualItem> List<T> findVirtualItems(Class<T> type);

    <T extends VirtualItem> List<T> findVirtualItems(Class<T> type, String controllerId);

    List<RuleItem> findRules();

    List<RuleItem> findRules(String controllerId);
}
