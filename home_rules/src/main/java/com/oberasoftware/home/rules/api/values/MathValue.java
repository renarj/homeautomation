package com.oberasoftware.home.rules.api.values;

import com.oberasoftware.home.rules.api.MathOperator;

/**
 * @author Renze de Vries
 */
public class MathValue implements ResolvableValue {
    private ResolvableValue leftValue;
    private ResolvableValue rightValue;
    private MathOperator operator;

    public MathValue(ResolvableValue leftValue, ResolvableValue rightValue, MathOperator operator) {
        this.leftValue = leftValue;
        this.rightValue = rightValue;
        this.operator = operator;
    }

    public MathValue() {
    }

    public ResolvableValue getLeftValue() {
        return leftValue;
    }

    public void setLeftValue(ResolvableValue leftValue) {
        this.leftValue = leftValue;
    }

    public ResolvableValue getRightValue() {
        return rightValue;
    }

    public void setRightValue(ResolvableValue rightValue) {
        this.rightValue = rightValue;
    }

    public MathOperator getOperator() {
        return operator;
    }

    public void setOperator(MathOperator operator) {
        this.operator = operator;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MathValue mathValue = (MathValue) o;

        if (!leftValue.equals(mathValue.leftValue)) return false;
        if (!rightValue.equals(mathValue.rightValue)) return false;
        return operator == mathValue.operator;

    }

    @Override
    public int hashCode() {
        int result = leftValue.hashCode();
        result = 31 * result + rightValue.hashCode();
        result = 31 * result + operator.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "MathValue{" +
                "leftValue=" + leftValue +
                ", rightValue=" + rightValue +
                ", operator=" + operator +
                '}';
    }
}
