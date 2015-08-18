package com.oberasoftware.home.rules.evaluators.values;

import com.oberasoftware.home.api.types.VALUE_TYPE;
import com.oberasoftware.home.api.types.Value;
import com.oberasoftware.home.rules.api.values.MathValue;
import com.oberasoftware.home.rules.api.values.ResolvableValue;
import com.oberasoftware.home.rules.evaluators.EvalException;
import com.oberasoftware.home.rules.evaluators.EvaluatorFactory;
import com.oberasoftware.home.rules.evaluators.conditions.NumberTypeOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Renze de Vries
 */
@Component
public class MathValueEvaluator implements ValueEvaluator<MathValue> {

    private static final NumberTypeOperator NUMBER_COMPARATOR = new NumberTypeOperator();

    @Autowired
    private EvaluatorFactory evaluatorFactory;

    @Override
    public Value eval(MathValue input) {
        Value leftValue = resolve(input.getLeftValue());
        Value rightValue = resolve(input.getRightValue());

        if(leftValue.getType() == VALUE_TYPE.NUMBER && rightValue.getType() == VALUE_TYPE.NUMBER) {
            switch(input.getOperator()) {
                case PLUS:
                    return NUMBER_COMPARATOR.plus(leftValue, rightValue);
                case MINUS:
                    return NUMBER_COMPARATOR.minus(leftValue, rightValue);
                default:
                    throw new EvalException("Unsupport operator: " + input.getOperator());
            }
        } else {
            throw new EvalException("Math operation can only be done on numbers");
        }
    }

    @Override
    public Set<String> getDependentItems(MathValue input) {
        Set<String> l = getDependentItems(input.getLeftValue());
        Set<String> r = getDependentItems(input.getRightValue());

        Set<String> d = new HashSet<>();
        d.addAll(l);
        d.addAll(r);

        return d;
    }

    private Value resolve(ResolvableValue value) {
        ValueEvaluator<ResolvableValue> valueEvaluator = evaluatorFactory.getEvaluator(value);
        return valueEvaluator.eval(value);
    }

    private Set<String> getDependentItems(ResolvableValue value) {
        ValueEvaluator<ResolvableValue> valueEvaluator = evaluatorFactory.getEvaluator(value);
        return valueEvaluator.getDependentItems(value);
    }
}
