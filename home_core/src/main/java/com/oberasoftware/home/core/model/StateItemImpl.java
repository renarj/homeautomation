package com.oberasoftware.home.core.model;

import com.oberasoftware.home.api.model.StateItem;
import com.oberasoftware.home.api.types.Value;

/**
 * @author renarj
 */
public class StateItemImpl implements StateItem {

    private final String label;
    private final Value value;

    public StateItemImpl(String label, Value value) {
        this.label = label;
        this.value = value;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public Value getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "StateItemImpl{" +
                "label='" + label + '\'' +
                ", value=" + value +
                '}';
    }
}
