package com.oberasoftware.home.zwave.api;

import com.oberasoftware.home.api.exceptions.HomeAutomationException;

/**
 * @author renarj
 */
public interface ZWaveSession {
    DeviceManager getDeviceManager();

    void doAction(ZWaveAction action) throws HomeAutomationException;
}
