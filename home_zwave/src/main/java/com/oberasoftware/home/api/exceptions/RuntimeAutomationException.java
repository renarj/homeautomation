package com.oberasoftware.home.api.exceptions;

/**
 * @author renarj
 */
public class RuntimeAutomationException extends RuntimeException {
    public RuntimeAutomationException(String message, Throwable e) {
        super(message, e);
    }
}
