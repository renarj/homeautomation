package com.oberasoftware.exceptions;

import com.oberasoftware.home.api.exceptions.HomeAutomationException;

/**
 * @author renarj
 */
public class MonitorException extends HomeAutomationException {
    public MonitorException(String message, Throwable e) {
        super(message, e);
    }
}
