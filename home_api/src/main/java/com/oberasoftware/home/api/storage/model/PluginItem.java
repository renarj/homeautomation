package com.oberasoftware.home.api.storage.model;

import java.util.Map;

/**
 * @author renarj
 */
public class PluginItem implements Item {
    private final String controllerId;
    private final String pluginId;

    private final String id;
    private final String name;

    private final Map<String, String> properties;

    public PluginItem(String id, String controllerId, String pluginId, String name, Map<String, String> properties) {
        this.controllerId = controllerId;
        this.pluginId = pluginId;
        this.id = id;
        this.name = name;
        this.properties = properties;
    }

    @Override
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getControllerId() {
        return controllerId;
    }

    public String getPluginId() {
        return pluginId;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    @Override
    public String toString() {
        return "DevicePlugin{" +
                ", controllerId='" + controllerId + '\'' +
                ", pluginId='" + pluginId + '\'' +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
