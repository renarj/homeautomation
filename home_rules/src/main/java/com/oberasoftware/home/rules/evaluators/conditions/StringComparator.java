package com.oberasoftware.home.rules.evaluators.conditions;

import com.oberasoftware.home.api.types.Value;

/**
 * @author Renze de Vries
 */
public class StringComparator implements Comparator {
    @Override
    public boolean equals(Value left, Value right) {
        return left.asString().equals(right.asString());
    }

    @Override
    public boolean largerThan(Value left, Value right) {
        return false;
    }

    @Override
    public boolean largerThanEquals(Value left, Value right) {
        return false;
    }

    @Override
    public boolean smallerThan(Value left, Value right) {
        return false;
    }

    @Override
    public boolean smallerThanEquals(Value left, Value right) {
        return false;
    }
}
