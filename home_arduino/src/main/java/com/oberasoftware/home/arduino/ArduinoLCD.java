package com.oberasoftware.home.arduino;

/**
 * @author renarj
 */
public interface ArduinoLCD {

    enum LINE {
        FIRST,
        SECOND
    }

    void write(LINE line, String text);
}
