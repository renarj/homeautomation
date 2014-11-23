package com.oberasoftware.home.api.exceptions;

/**
 * @author renarj
 */
public class RuntimeEventException extends RuntimeAutomationException {
    public RuntimeEventException(String message, Throwable e) {
        super(message, e);
    }
}
