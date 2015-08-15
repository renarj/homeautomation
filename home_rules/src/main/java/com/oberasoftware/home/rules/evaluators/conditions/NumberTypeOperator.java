package com.oberasoftware.home.rules.evaluators.conditions;

import com.google.common.base.Preconditions;
import com.oberasoftware.home.api.types.VALUE_TYPE;
import com.oberasoftware.home.api.types.Value;
import com.oberasoftware.home.core.types.ValueImpl;
import com.oberasoftware.home.rules.evaluators.EvalException;

/**
 * @author Renze de Vries
 */
public class NumberTypeOperator implements TypeOperator {
    @Override
    public boolean equals(Value left, Value right) {
        assertInput(left, right);
        return toLong(left) == toLong(right);
    }

    @Override
    public boolean largerThan(Value left, Value right) {
        return toDouble(left) > toDouble(right);
    }

    @Override
    public boolean largerThanEquals(Value left, Value right) {
        return toDouble(left) >= toDouble(right);
    }

    @Override
    public boolean smallerThan(Value left, Value right) {
        return toDouble(left) < toDouble(right);
    }

    @Override
    public boolean smallerThanEquals(Value left, Value right) {
        return toDouble(left) <= toDouble(right);
    }

    @Override
    public Value plus(Value left, Value right) {
        return new ValueImpl(VALUE_TYPE.NUMBER, toLong(left) + toLong(right));
    }

    @Override
    public Value minus(Value left, Value right) {
        return new ValueImpl(VALUE_TYPE.NUMBER, toLong(left) - toLong(right));
    }

    private void assertInput(Value left, Value right) {
        Preconditions.checkNotNull(left);
        Preconditions.checkNotNull(right);

        if(isDecimalBased(left) || isDecimalBased(right)) {
            throw new EvalException("Cannot compare floating numbers: " + left.getValue() + " / " + right.getValue() + " on equality");
        }
    }

    private boolean isDecimalBased(Value value) {
        Object rawValue = value.getValue();

        return rawValue instanceof Double || rawValue instanceof Float;
    }

    private long toLong(Value value) {
        Object rawValue = value.getValue();
        if(rawValue instanceof Long) {
            return (Long) rawValue;
        } else if(rawValue instanceof Integer) {
            return (Integer) rawValue;
        } else if(rawValue instanceof String) {
            try {
                return Long.parseLong(rawValue.toString());
            } catch(NumberFormatException e) {
                throw new EvalException("Unable to evaluate value: " + value + " (Not a Number)");
            }
        } else {
            throw new EvalException("Unable to evaluate value: " + value + ", unsupported data type: " + rawValue.getClass());
        }

    }

    private double toDouble(Value value) {
        Object rawValue = value.getValue();
        if(rawValue instanceof Long) {
            return (Long) rawValue;
        } else if(rawValue instanceof Integer) {
            return (Integer) rawValue;
        } else if(rawValue instanceof Double) {
            return (Double) rawValue;
        } else if(rawValue instanceof String) {
            try {
                return Double.parseDouble(rawValue.toString());
            } catch(NumberFormatException e) {
                throw new EvalException("Unable to evaluate value: " + value + " (Not a Number)");
            }
        } else {
            throw new EvalException("Unable to evaluate value: " + value + ", unsupported data type: " + rawValue.getClass());
        }
    }
}
