package com.oberasoftware.home.api.events.devices;

import com.oberasoftware.home.api.events.Event;
import com.oberasoftware.home.api.model.Device;

/**
 * @author renarj
 */
public class DeviceInformationEvent implements Event {

    public enum EVENT_TYPE {
        NEW_DEVICE,
        UPDATED_DEVICE,
        REMOVED_DEVICE
    }

    private final EVENT_TYPE type;
    private final String pluginId;
    private final Device device;

    public DeviceInformationEvent(EVENT_TYPE type, String pluginId, Device device) {
        this.type = type;
        this.pluginId = pluginId;
        this.device = device;
    }

    public EVENT_TYPE getType() {
        return type;
    }

    public String getPluginId() {
        return pluginId;
    }

    public Device getDevice() {
        return device;
    }

    @Override
    public String toString() {
        return "DeviceInformationEvent{" +
                "device=" + device +
                ", pluginId='" + pluginId + '\'' +
                ", type=" + type +
                '}';
    }
}
