package com.oberasoftware.home.zwave.exceptions;

/**
 * @author renarj
 */
public class ZWaveException extends Exception {
    public ZWaveException(String message, Throwable e) {
        super(message, e);
    }
}
