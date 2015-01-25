package com.oberasoftware.home.api.events.devices;

import com.oberasoftware.home.api.types.Value;

/**
 * @author renarj
 */
public class DeviceNumericValueEvent implements DeviceValueEvent {
    private final String controllerId;
    private final String pluginId;
    private final String deviceId;
    private final Value value;
    private final String label;

    public DeviceNumericValueEvent(String controllerId, String pluginId, String deviceId, Value value, String label) {
        this.controllerId = controllerId;
        this.pluginId = pluginId;
        this.deviceId = deviceId;
        this.value = value;
        this.label = label;
    }

    @Override
    public String getControllerId() {
        return controllerId;
    }

    @Override
    public String getPluginId() {
        return pluginId;
    }

    @Override
    public String getDeviceId() {
        return deviceId;
    }

    @Override
    public Value getValue() {
        return value;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return "DeviceNumericValueEvent{" +
                "controllerId='" + controllerId + '\'' +
                ", pluginId='" + pluginId + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", value=" + value +
                ", label='" + label + '\'' +
                '}';
    }
}
