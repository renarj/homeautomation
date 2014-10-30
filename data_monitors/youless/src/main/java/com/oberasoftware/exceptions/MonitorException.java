package com.oberasoftware.exceptions;

/**
 * @author renarj
 */
public class MonitorException extends Exception {
    public MonitorException(String message, Throwable e) {
        super(message, e);
    }
}
