package com.oberasoftware.home.arduino;

import com.oberasoftware.home.api.exceptions.HomeAutomationException;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author renarj
 */
public interface ArduinoConnector {
    void connect();

    boolean isConnected();

    InputStream getInputStream() throws HomeAutomationException;

    OutputStream getOutputStream() throws HomeAutomationException;

    void disconnect();
}
