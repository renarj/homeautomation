package com.oberasoftware.home.api.storage.model;

import java.util.HashMap;
import java.util.Map;

/**
 * @author renarj
 */
public class UIItem implements Item {
    private String id;
    private String name;
    private String description;
    private String uiType;

    private String containerId;

    private String deviceId;

    private long weight = 0;

    private Map<String, String> properties = new HashMap<>();

    public UIItem(String id, String name, String containerId, String description, String uiType, String deviceId, Map<String, String> properties, long weight) {
        this.id = id;
        this.name = name;
        this.containerId = containerId;
        this.description = description;
        this.uiType = uiType;
        this.deviceId = deviceId;
        this.properties = properties;
        this.weight = weight;
    }

    public UIItem() {
    }

    public long getWeight() {
        return weight;
    }

    public void setWeight(long weight) {
        this.weight = weight;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUiType(String uiType) {
        this.uiType = uiType;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getContainerId() {
        return containerId;
    }

    @Override
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getUiType() {
        return uiType;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    public void addProperty(String property, String value) {
        this.properties.put(property, value);
    }

    @Override
    public String toString() {
        return "UIItem{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", uiType='" + uiType + '\'' +
                ", containerId='" + containerId + '\'' +
                ", deviceId='" + deviceId + '\'' +
                '}';
    }
}
