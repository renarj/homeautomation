package com.oberasoftware.home.api.storage.model;

/**
 * @author renarj
 */
public class ControllerItem implements Item {
    private final String controllerId;
    private final String id;

    public ControllerItem(String id, String controllerId) {
        this.id = id;
        this.controllerId = controllerId;
    }

    public String getControllerId() {
        return controllerId;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "ControllerItem{" +
                ", controllerId='" + controllerId + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
