package com.oberasoftware.home.rules.evaluators.conditions;

import com.oberasoftware.home.api.types.Value;

/**
 * @author Renze de Vries
 */
public interface TypeOperator {
    boolean equals(Value left, Value right);

    boolean largerThan(Value left, Value right);

    boolean largerThanEquals(Value left, Value right);

    boolean smallerThan(Value left, Value right);

    boolean smallerThanEquals(Value left, Value right);

    Value plus(Value left, Value right);

    Value minus(Value left, Value right);
}
