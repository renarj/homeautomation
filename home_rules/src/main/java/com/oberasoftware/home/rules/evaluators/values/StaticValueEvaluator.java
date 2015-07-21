package com.oberasoftware.home.rules.evaluators.values;

import com.oberasoftware.home.api.types.VALUE_TYPE;
import com.oberasoftware.home.api.types.Value;
import com.oberasoftware.home.core.types.ValueImpl;
import com.oberasoftware.home.rules.api.StaticValue;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Renze de Vries
 */
@Component
public class StaticValueEvaluator implements ValueEvaluator<StaticValue> {
    @Override
    public Value eval(StaticValue input) {
        Object val = input.getValue();
        if(val instanceof String) {
            String value = val.toString();
            if(value.equals("detected")) {
                value = "on";
            } else if(value.equals("not detected")) {
                value = "off";
            }

            return new ValueImpl(VALUE_TYPE.STRING, value);
        }

        return new ValueImpl(input.getType(), input.getValue());
    }

    @Override
    public Set<String> getDependentItems(StaticValue input) {
        return new HashSet<>();
    }
}
