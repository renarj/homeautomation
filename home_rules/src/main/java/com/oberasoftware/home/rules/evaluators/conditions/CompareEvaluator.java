package com.oberasoftware.home.rules.evaluators.conditions;

import com.oberasoftware.home.api.types.VALUE_TYPE;
import com.oberasoftware.home.api.types.Value;
import com.oberasoftware.home.rules.api.logic.CompareCondition;
import com.oberasoftware.home.rules.api.Operator;
import com.oberasoftware.home.rules.api.values.ResolvableValue;
import com.oberasoftware.home.rules.evaluators.EvalException;
import com.oberasoftware.home.rules.evaluators.EvaluatorFactory;
import com.oberasoftware.home.rules.evaluators.values.ValueEvaluator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Renze de Vries
 */
@Component
public class CompareEvaluator implements ConditionEvaluator<CompareCondition> {

    private static final NumberTypeOperator NUMBER_COMPARATOR = new NumberTypeOperator();
    private static final StringTypeOperator STRING_COMPARATOR = new StringTypeOperator();

    @Autowired
    private EvaluatorFactory evaluatorFactory;

    @Override
    public Set<String> getDependentItems(CompareCondition input) {
        Set<String> l = getDependentItems(input.getLeftValue());
        Set<String> r = getDependentItems(input.getRightValue());

        Set<String> d = new HashSet<>();
        d.addAll(l);
        d.addAll(r);

        return d;
    }

    @Override
    public Boolean eval(CompareCondition input) {
        Value leftValue = resolve(input.getLeftValue());
        Value rightValue = resolve(input.getRightValue());

        return compareValues(leftValue, rightValue, input.getOperator());
    }

    private Set<String> getDependentItems(ResolvableValue value) {
        ValueEvaluator<ResolvableValue> valueEvaluator = evaluatorFactory.getEvaluator(value);
        return valueEvaluator.getDependentItems(value);
    }

    private Value resolve(ResolvableValue value) {
        ValueEvaluator<ResolvableValue> valueEvaluator = evaluatorFactory.getEvaluator(value);
        return valueEvaluator.eval(value);
    }

    private boolean compareValues(Value left, Value right, Operator operator) {
        if(left.getType() == VALUE_TYPE.NUMBER || right.getType() == VALUE_TYPE.NUMBER) {
            return runComparator(operator, left, right, NUMBER_COMPARATOR);
        } else if(left.getType() == VALUE_TYPE.STRING || right.getType() == VALUE_TYPE.STRING) {
            return runComparator(operator, left, right, STRING_COMPARATOR);
        } else {
            throw new EvalException("No support for data types: " + left.getValue() + " / " + right.getValue());
        }
    }

    private boolean runComparator(Operator operator, Value left, Value right, TypeOperator typeOperator) {
        switch(operator) {
            case EQUALS:
                return typeOperator.equals(left, right);
            case LARGER_THAN:
                return typeOperator.largerThan(left, right);
            case LARGER_THAN_EQUALS:
                return typeOperator.largerThanEquals(left, right);
            case SMALLER_THAN:
                return typeOperator.smallerThan(left, right);
            case SMALLER_THAN_EQUALS:
                return typeOperator.smallerThanEquals(left, right);
            default:
                throw new EvalException("Unsupport operator: " + operator);
        }
    }
}
