package com.oberasoftware.home.arduino;

import com.oberasoftware.home.api.exceptions.HomeAutomationException;
import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author renarj
 */
@Component
public class SerialArduinoConnector implements ArduinoConnector {
    private static final Logger LOG = getLogger(SerialArduinoConnector.class);

    private SerialPort serialPort;

    private String port = "/dev/tty.usbmodem1451";

    @Override
    public void connect() {
        try {
            LOG.debug("Connecting to Serial port Arduino: {}", port);
            CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(port);

            CommPort commPort = portIdentifier.open("arduino", 2000);
            this.serialPort = (SerialPort) commPort;
            this.serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
            this.serialPort.enableReceiveThreshold(1);
            this.serialPort.enableReceiveTimeout(5000);
            LOG.info("Connected to arduino: {}", port);
        } catch (NoSuchPortException | PortInUseException | UnsupportedCommOperationException e) {
            LOG.error("", e);
        }
    }

    @Override
    public boolean isConnected() {
        return serialPort != null;
    }

    @Override
    public InputStream getInputStream() throws HomeAutomationException {
        try {
            return serialPort.getInputStream();
        } catch (IOException e) {
            throw new HomeAutomationException("Unable to get input stream to arduino", e);
        }
    }

    @Override
    public OutputStream getOutputStream() throws HomeAutomationException {
        try {
            return serialPort.getOutputStream();
        } catch (IOException e) {
            throw new HomeAutomationException("Unable to get output stream to arduino", e);
        }
    }

    @Override
    public void disconnect() {
        serialPort.close();
    }
}
