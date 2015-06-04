package com.oberasoftware.home.api.types;

/**
 * @author renarj
 */
public interface Value {
    VALUE_TYPE getType();

    <T> T getValue();

    String asString();
}
