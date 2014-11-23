package com.oberasoftware.home.api.exceptions;

/**
 * @author renarj
 */
public class HomeAutomationException extends Exception {
    public HomeAutomationException(String message, Throwable e) {
        super(message, e);
    }

    public HomeAutomationException(String message) {
        super(message);
    }
}
