package com.oberasoftware.home.rules.evaluators.conditions;

import com.oberasoftware.home.api.types.Value;
import com.oberasoftware.home.rules.evaluators.EvalException;

/**
 * @author Renze de Vries
 */
public class StringTypeOperator implements TypeOperator {
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

    @Override
    public Value plus(Value left, Value right) {
        throw new EvalException("No Math support for Text");
    }

    @Override
    public Value minus(Value left, Value right) {
        throw new EvalException("No Math support for Text");
    }
}
