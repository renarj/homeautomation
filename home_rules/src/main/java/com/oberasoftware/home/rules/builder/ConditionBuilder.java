package com.oberasoftware.home.rules.builder;

import com.google.common.collect.Lists;
import com.oberasoftware.home.api.types.VALUE_TYPE;
import com.oberasoftware.home.rules.api.logic.CompareCondition;
import com.oberasoftware.home.rules.api.Condition;
import com.oberasoftware.home.rules.api.values.ItemValue;
import com.oberasoftware.home.rules.api.logic.LogicCondition;
import com.oberasoftware.home.rules.api.Operator;
import com.oberasoftware.home.rules.api.values.ResolvableValue;
import com.oberasoftware.home.rules.api.values.StaticValue;

/**
 * @author Renze de Vries
 */
public class ConditionBuilder {

    private ResolvableValue leftValue;
    private ResolvableValue rightValue;
    private Operator operator;

    private LogicCondition.TYPE type;
    private ConditionBuilder left;
    private ConditionBuilder right;


    public static ConditionBuilder create() {
        return new ConditionBuilder();
    }

    public ConditionBuilder and(ConditionBuilder left, ConditionBuilder right) {
        this.type = LogicCondition.TYPE.AND;
        this.left = left;
        this.right = right;
        return this;
    }

    public ConditionBuilder itemValue(String itemId, String label) {
        this.leftValue = new ItemValue(itemId, label);
        return this;
    }

    public ConditionBuilder compare(Operator operator, String value) {
        this.operator = operator;
        this.rightValue = new StaticValue(value, VALUE_TYPE.STRING);
        return this;
    }

    public ConditionBuilder compare(Operator operator, long number) {
        this.operator = operator;
        this.rightValue = new StaticValue(number, VALUE_TYPE.NUMBER);
        return this;
    }

    public Condition build() {
        if (left != null && right != null) {
            return new LogicCondition(type, Lists.newArrayList(left.build(), right.build()));
        } else {
            return new CompareCondition(leftValue, operator, rightValue);
        }
    }
}
