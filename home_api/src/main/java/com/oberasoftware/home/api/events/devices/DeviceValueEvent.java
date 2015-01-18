package com.oberasoftware.home.api.events.devices;

import com.oberasoftware.home.api.events.Event;
import com.oberasoftware.home.api.types.Value;

/**
 * @author renarj
 */
public class DeviceValueEvent implements Event {
    private final String controllerId;
    private final String pluginId;
    private final String deviceId;
    private final Value value;
    private final String label;

    public DeviceValueEvent(String controllerId, String pluginId, String deviceId, Value value, String label) {
        this.controllerId = controllerId;
        this.pluginId = pluginId;
        this.deviceId = deviceId;
        this.value = value;
        this.label = label;
    }

    public String getControllerId() {
        return controllerId;
    }

    public String getPluginId() {
        return pluginId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public Value getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }
}
