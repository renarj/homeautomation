package com.oberasoftware.home.core.types;

import com.oberasoftware.home.api.types.VALUE_TYPE;
import com.oberasoftware.home.api.types.Value;

/**
 * @author renarj
 */
public class ValueImpl implements Value {
    
    private final Object value;
    private final VALUE_TYPE type;

    public ValueImpl(VALUE_TYPE type, Object value) {
        this.type = type;
        this.value = value;
    }

    @Override
    public VALUE_TYPE getType() {
        return type;
    }

    @Override
    public <T> T getValue() {
        return (T) value;
    }

    @Override
    public String asString() {
        return value.toString();
    }

    @Override
    public String toString() {
        return "ValueImpl{" +
                "value=" + value +
                ", type=" + type +
                '}';
    }
}
