package com.oberasoftware.home.api.commands;

/**
 * @author renarj
 */
public interface ValueCommand extends BasicCommand {

    enum VALUE_TYPE {
        NUMBER,
        DECIMAL,
        STRING
    }
    
    VALUE_TYPE getValueType();

    <T> T getValue();
}
