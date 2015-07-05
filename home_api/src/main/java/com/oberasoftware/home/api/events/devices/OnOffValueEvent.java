package com.oberasoftware.home.api.events.devices;

import com.oberasoftware.home.api.events.OnOffValue;
import com.oberasoftware.home.api.types.Value;

/**
 * @author renarj
 */
public class OnOffValueEvent implements DeviceValueEvent {
    private final String controllerId;
    private final String pluginId;
    private final String deviceId;

    private final OnOffValue onOffValue;

    public OnOffValueEvent(String controllerId, String pluginId, String deviceId, boolean on) {
        this.controllerId = controllerId;
        this.pluginId = pluginId;
        this.deviceId = deviceId;
        this.onOffValue = new OnOffValue(on);
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
        return onOffValue.isOn();
    }

    @Override
    public String getLabel() {
        return OnOffValue.LABEL;
    }

    @Override
    public Value getValue() {
        return onOffValue;
    }
}
