package com.oberasoftware.home.rules.api;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

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
}
