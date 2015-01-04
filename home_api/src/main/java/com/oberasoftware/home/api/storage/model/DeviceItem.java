package com.oberasoftware.home.api.storage.model;

import java.util.Map;

/**
 * @author renarj
 */
public class DeviceItem implements Item {
    private final String id;
    private final String pluginId;
    private final String deviceId;
    private final String name;

    private final Map<String, String> properties;

    public DeviceItem(String id, String pluginId, String deviceId, String name, Map<String, String> properties) {
        this.pluginId = pluginId;
        this.id = id;
        this.name = name;
        this.deviceId = deviceId;
        this.properties = properties;
    }

    public String getPluginId() {
        return pluginId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getName() {
        return name;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "DeviceItem{" +
                "id='" + id + '\'' +
                ", pluginId='" + pluginId + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", name='" + name + '\'' +
                ", properties=" + properties +
                '}';
    }
}
