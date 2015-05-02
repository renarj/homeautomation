package com.oberasoftware.home.api.storage.model;

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

    public UIItem(String id, String name, String containerId, String description, String uiType, String deviceId) {
        this.id = id;
        this.name = name;
        this.containerId = containerId;
        this.description = description;
        this.uiType = uiType;
        this.deviceId = deviceId;
    }

    public UIItem() {
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
