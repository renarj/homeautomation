package com.oberasoftware.home.zwave.api.events;

import com.oberasoftware.home.zwave.api.ZWaveAction;
import com.oberasoftware.home.zwave.api.ZWaveDeviceAction;

/**
 * @author renarj
 */
public class WaitForWakeUpEvent implements ZWaveAction {
    private final ZWaveDeviceAction deviceAction;

    public WaitForWakeUpEvent(ZWaveDeviceAction deviceAction) {
        this.deviceAction = deviceAction;
    }

    public ZWaveDeviceAction getDeviceAction() {
        return deviceAction;
    }
}
