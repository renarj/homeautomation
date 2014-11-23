package com.oberasoftware.home.zwave.exceptions;

import com.oberasoftware.home.api.exceptions.HomeAutomationException;

/**
 * @author renarj
 */
public class ZWaveException extends HomeAutomationException {
    public ZWaveException(String message, Throwable e) {
        super(message, e);
    }

    public ZWaveException(String message) {
        super(message);
    }
}
