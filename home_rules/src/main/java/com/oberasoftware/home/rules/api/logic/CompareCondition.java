package com.oberasoftware.home.rules.api.logic;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.oberasoftware.home.rules.api.Condition;
import com.oberasoftware.home.rules.api.Operator;
import com.oberasoftware.home.rules.api.values.ResolvableValue;

/**
 * @author Renze de Vries
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "type")
public class CompareCondition implements Condition {

    private ResolvableValue leftValue;
    private ResolvableValue rightValue;
    private Operator operator;

    public CompareCondition(ResolvableValue leftValue, Operator operator, ResolvableValue rightValue) {
        this.leftValue = leftValue;
        this.rightValue = rightValue;
        this.operator = operator;
    }

    public CompareCondition() {
    }

    public ResolvableValue getLeftValue() {
        return leftValue;
    }

    public void setLeftValue(ResolvableValue leftValue) {
        this.leftValue = leftValue;
    }

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public ResolvableValue getRightValue() {
        return rightValue;
    }

    public void setRightValue(ResolvableValue rightValue) {
        this.rightValue = rightValue;
    }

    @Override
    public String toString() {
        return "CompareCondition{" +
                "leftValue=" + leftValue +
                ", rightValue=" + rightValue +
                ", operator=" + operator +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CompareCondition that = (CompareCondition) o;

        if (!leftValue.equals(that.leftValue)) return false;
        if (!rightValue.equals(that.rightValue)) return false;
        return operator == that.operator;

    }

    @Override
    public int hashCode() {
        int result = leftValue.hashCode();
        result = 31 * result + rightValue.hashCode();
        result = 31 * result + operator.hashCode();
        return result;
    }
}
