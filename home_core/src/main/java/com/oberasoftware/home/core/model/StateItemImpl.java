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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StateItemImpl stateItem = (StateItemImpl) o;

        if (!label.equals(stateItem.label)) return false;
        return value.equals(stateItem.value);

    }

    @Override
    public int hashCode() {
        int result = label.hashCode();
        result = 31 * result + value.hashCode();
        return result;
    }
}
