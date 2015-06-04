package com.oberasoftware.home.api.exceptions;

/**
 * @author renarj
 */
public class DataStoreException extends HomeAutomationException {
    public DataStoreException(String message, Throwable e) {
        super(message, e);
    }

    public DataStoreException(String message) {
        super(message);
    }
}
