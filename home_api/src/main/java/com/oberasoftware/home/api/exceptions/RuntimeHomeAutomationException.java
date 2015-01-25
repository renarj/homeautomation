package com.oberasoftware.home.api.exceptions;

/**
 * @author renarj
 */
public class RuntimeHomeAutomationException extends RuntimeException {
    public RuntimeHomeAutomationException(String message, Throwable e) {
        super(message, e);
    }

    public RuntimeHomeAutomationException(String message) {
        super(message);
    }
}
