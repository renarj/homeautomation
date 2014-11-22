package com.oberasoftware.home.zwave.api.actions;

import com.oberasoftware.home.zwave.api.ZWaveDevice;
import com.oberasoftware.home.zwave.api.ZWaveDeviceAction;

/**
 * @author renarj
 */
public class SwitchAction implements ZWaveDeviceAction {

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

    @Override
    public int getNodeId() {
        return device.getNodeId();
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
