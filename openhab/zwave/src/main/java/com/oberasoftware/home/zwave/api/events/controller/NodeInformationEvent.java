package com.oberasoftware.home.zwave.api.events.controller;

import com.oberasoftware.home.zwave.api.events.ControllerEvent;

/**
 * @author renarj
 */
public class NodeInformationEvent implements ControllerEvent {
    @Override
    public boolean isTransactionCompleted() {
        return true;
    }
}
