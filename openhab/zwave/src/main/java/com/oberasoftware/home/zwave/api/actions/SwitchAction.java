package com.oberasoftware.home.zwave.api.actions;

import com.oberasoftware.home.zwave.api.ZWaveAction;
import com.oberasoftware.home.zwave.api.ZWaveDevice;

/**
 * @author renarj
 */
public class SwitchAction implements ZWaveAction {

    public enum STATE {
        ON,
        OFF
    }

    private final STATE desiredState;
    private final ZWaveDevice device;

    public SwitchAction(ZWaveDevice device, STATE desiredState) {
        this.device = device;
        this.desiredState = desiredState;
    }

    public STATE getDesiredState() {
        return desiredState;
    }

    public ZWaveDevice getDevice() {
        return device;
    }

    @Override
    public String toString() {
        return "SwitchAction{" +
                "desiredState=" + desiredState +
                ", device=" + device +
                '}';
    }
}
