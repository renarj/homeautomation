package com.oberasoftware.home.rules.evaluators.conditions;

import com.oberasoftware.home.rules.api.Condition;
import com.oberasoftware.home.rules.api.logic.LogicCondition;
import com.oberasoftware.home.rules.evaluators.EvaluatorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Renze de Vries
 */
@Component
public class LogicEvaluator implements ConditionEvaluator<LogicCondition> {

    @Autowired
    private EvaluatorFactory evaluatorFactory;

    @Override
    public Boolean eval(LogicCondition input) {
        List<Condition> conditions = input.getConditions();
        if(input.getType() == LogicCondition.TYPE.AND) {
            return conditions.stream().allMatch(c -> {
                ConditionEvaluator<Condition> e = evaluatorFactory.getEvaluator(c);
                return e.eval(c);
            });
        } else {
            return conditions.stream().anyMatch(c -> {
                ConditionEvaluator<Condition> e = evaluatorFactory.getEvaluator(c);
                return e.eval(c);
            });
        }
    }

    @Override
    public Set<String> getDependentItems(LogicCondition input) {
        Set<String> dependentItems = new HashSet<>();
        input.getConditions().forEach(c -> {
            ConditionEvaluator<Condition> e = evaluatorFactory.getEvaluator(c);

            dependentItems.addAll(e.getDependentItems(c));
        });

        return dependentItems;
    }
}
