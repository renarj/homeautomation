package com.oberasoftware.home.rules.evaluators.blocks;

import com.oberasoftware.home.rules.api.Action;
import com.oberasoftware.home.rules.api.Condition;
import com.oberasoftware.home.rules.api.IfBlock;
import com.oberasoftware.home.rules.evaluators.EvaluatorFactory;
import com.oberasoftware.home.rules.evaluators.actions.ActionEvaluator;
import com.oberasoftware.home.rules.evaluators.conditions.ConditionEvaluator;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Renze de Vries
 */
@Component
public class IfBlockEvaluator implements BlockEvaluator<IfBlock> {
    private static final Logger LOG = getLogger(IfBlockEvaluator.class);

    @Autowired
    private EvaluatorFactory evaluatorFactory;

    @Override
    public Boolean eval(IfBlock input) {
        ConditionEvaluator<Condition> conditionEvaluator = evaluatorFactory.getEvaluator(input.getCondition());
        boolean eval = conditionEvaluator.eval(input.getCondition());

        if(eval) {
            LOG.debug("Condition: {} is true actions: {} will be executed", input.getCondition(), input.getActions());
            evalActions(input.getActions());
        }

        return eval;
    }

    private void evalActions(List<Action> actions) {
        actions.forEach(a -> {
            ActionEvaluator<Action> evaluator =evaluatorFactory.getEvaluator(a);

            boolean eval = evaluator.eval(a);
            LOG.debug("Action: {} evaluated: {}", a, eval);
        });
    }

    @Override
    public Set<String> getDependentItems(IfBlock input) {
        ConditionEvaluator<Condition> conditionEvaluator = evaluatorFactory.getEvaluator(input.getCondition());
        return conditionEvaluator.getDependentItems(input.getCondition());
    }
}
