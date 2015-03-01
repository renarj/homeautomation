package com.oberasoftware.home.api.storage.model;

/**
 * @author renarj
 */
public class UIItem implements Item {
    private final String id;
    private final String name;
    private final String description;
    private final String uiType;

    private final String deviceId;

    public UIItem(String id, String name, String description, String uiType, String deviceId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.uiType = uiType;
        this.deviceId = deviceId;
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
}
