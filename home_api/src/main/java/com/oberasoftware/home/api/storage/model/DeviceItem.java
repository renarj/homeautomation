package com.oberasoftware.home.api.storage.model;

import java.util.List;
import java.util.Map;

/**
 * @author renarj
 */
public class DeviceItem implements Item {
    private final String id;
    private final String pluginId;
    private final String deviceId;
    private final String name;
    private final String controllerId;

    private final Map<String, String> properties;

    private final Map<String, List<String>> configuration;

    public DeviceItem(String id, String controllerId, String pluginId, String deviceId, String name, Map<String, String> properties, Map<String, List<String>> configuration) {
        this.controllerId = controllerId;
        this.pluginId = pluginId;
        this.id = id;
        this.name = name;
        this.deviceId = deviceId;
        this.properties = properties;
        this.configuration = configuration;
    }

    public Map<String, List<String>> getConfiguration() {
        return configuration;
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

    public String getControllerId() {
        return controllerId;
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
