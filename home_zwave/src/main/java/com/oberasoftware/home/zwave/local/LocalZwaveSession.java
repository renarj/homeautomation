package com.oberasoftware.home.zwave.local;

import com.oberasoftware.home.api.exceptions.HomeAutomationException;
import com.oberasoftware.home.zwave.ZWaveController;
import com.oberasoftware.home.zwave.api.DeviceManager;
import com.oberasoftware.home.zwave.api.ZWaveAction;
import com.oberasoftware.home.zwave.api.ZWaveSession;

/**
 * @author renarj
 */
public class LocalZwaveSession implements ZWaveSession {

    public LocalZwaveSession() {

    }

    @Override
    public DeviceManager getDeviceManager() {
        return null;
    }

    @Override
    public void doAction(ZWaveAction action) throws HomeAutomationException {
        LocalSpringContainer.getBean(ZWaveController.class).send(action);
    }
}
