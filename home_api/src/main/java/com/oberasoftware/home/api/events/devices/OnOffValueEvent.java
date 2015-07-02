package com.oberasoftware.home.api.events.devices;

import com.oberasoftware.home.api.types.VALUE_TYPE;
import com.oberasoftware.home.api.types.Value;

/**
 * @author renarj
 */
public class OnOffValueEvent implements DeviceValueEvent {
    private final String controllerId;
    private final String pluginId;
    private final String deviceId;

    private final boolean on;

    public OnOffValueEvent(String controllerId, String pluginId, String deviceId, boolean on) {
        this.controllerId = controllerId;
        this.pluginId = pluginId;
        this.deviceId = deviceId;
        this.on = on;
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

    public boolean isOn() {
        return on;
    }

    @Override
    public String getLabel() {
        return "on-off";
    }

    @Override
    public Value getValue() {
        return new Value() {
            @Override
            public VALUE_TYPE getType() {
                return VALUE_TYPE.STRING;
            }

            @Override
            public String getValue() {
                return asString();
            }

            @Override
            public String asString() {
                return isOn() ? "on" : "off";
            }
        };
    }
}
