package com.oberasoftware.home.rules.evaluators.conditions;

import com.oberasoftware.home.api.types.VALUE_TYPE;
import com.oberasoftware.home.api.types.Value;
import com.oberasoftware.home.rules.api.CompareCondition;
import com.oberasoftware.home.rules.api.Operator;
import com.oberasoftware.home.rules.api.ResolvableValue;
import com.oberasoftware.home.rules.evaluators.EvalException;
import com.oberasoftware.home.rules.evaluators.EvaluatorFactory;
import com.oberasoftware.home.rules.evaluators.values.ValueEvaluator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * @author Renze de Vries
 */
@Component
public class CompareEvaluator implements ConditionEvaluator<CompareCondition> {

    private static final NumberComparator NUMBER_COMPARATOR = new NumberComparator();
    private static final StringComparator STRING_COMPARATOR = new StringComparator();

    @Autowired
    private EvaluatorFactory evaluatorFactory;

    @Override
    public Set<String> getDependentItems(CompareCondition input) {
        return new HashSet<>();
    }

    @Override
    public Boolean eval(CompareCondition input) {
        Value leftValue = resolve(input.getLeftValue());
        Value rightValue = resolve(input.getRightValue());

        return compareValues(leftValue, rightValue, input.getOperator());
    }

    private Value resolve(ResolvableValue value) {
        Optional<ValueEvaluator> valueEvaluator = evaluatorFactory.getEvaluator(value);
        if(valueEvaluator.isPresent()) {
            ValueEvaluator<ResolvableValue> evaluator = valueEvaluator.get();
            return evaluator.eval(value);
        } else {
            return null;
        }
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

    private boolean runComparator(Operator operator, Value left, Value right, Comparator comparator) {
        switch(operator) {
            case EQUALS:
                return comparator.equals(left, right);
            case LARGER_THAN:
                return comparator.largerThan(left, right);
            case LARGER_THAN_EQUALS:
                return comparator.largerThanEquals(left, right);
            case SMALLER_THAN:
                return comparator.smallerThan(left, right);
            case SMALLER_THAN_EQUALS:
                return comparator.smallerThanEquals(left, right);
            default:
                throw new EvalException("Unsupport operator: " + operator);
        }
    }
}
