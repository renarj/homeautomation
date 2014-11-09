package com.oberasoftware.home.zwave.api.events;

/**
 * @author renarj
 */
public interface ControllerEvent extends ZWaveEvent {
    boolean isTransactionCompleted();
}
