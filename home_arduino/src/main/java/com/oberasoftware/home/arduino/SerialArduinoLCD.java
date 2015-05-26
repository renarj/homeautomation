package com.oberasoftware.home.arduino;

import com.oberasoftware.home.api.exceptions.HomeAutomationException;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author renarj
 */
@Component
public class SerialArduinoLCD implements ArduinoLCD {
    private static final Logger LOG = getLogger(SerialArduinoLCD.class);

    @Autowired
    private SerialArduinoConnector serialArduinoConnector;

    private String firstLine = "";
    private String secondLine = "";

    @Override
    public void write(LINE line, String text) {
        String trimmedText = text;
        if(trimmedText.length() > 16) {
            trimmedText = text.substring(0, 16);
        }
        if(line == LINE.FIRST) {
            firstLine = trimmedText;
        } else {
            secondLine = trimmedText;
        }
        writeToDisplay();
    }

    private void writeToDisplay() {
        if(serialArduinoConnector.isConnected()) {
            String totalText = firstLine + "\n" + secondLine + "\n";
            LOG.debug("Writing text to LCD: {}", totalText);
            try {
                serialArduinoConnector.getOutputStream().write(totalText.getBytes());
            } catch (IOException | HomeAutomationException e) {
                LOG.error("Unable to write to LCD display", e);
            }
        } else {
            LOG.warn("Cannot write to Arduino LCD, not connected");
        }
    }
}
