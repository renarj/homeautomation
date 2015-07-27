package com.oberasoftware.home.rules.evaluators.blocks;

import com.oberasoftware.home.rules.api.Action;
import com.oberasoftware.home.rules.api.Condition;
import com.oberasoftware.home.rules.api.IfBlock;
import com.oberasoftware.home.rules.api.IfBranch;
import com.oberasoftware.home.rules.evaluators.EvaluatorFactory;
import com.oberasoftware.home.rules.evaluators.actions.ActionEvaluator;
import com.oberasoftware.home.rules.evaluators.conditions.ConditionEvaluator;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
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
        List<IfBranch> branches = input.getBranches();
        for(IfBranch branch: branches) {
            if(evalCondition(branch.getCondition(), branch.getActions())) {
                LOG.debug("Branch: {} evaluated to true", branch);

                return true;
            }
        }

        return false;
    }

    private boolean evalCondition(Condition condition, List<Action> actions) {
        boolean eval = false;
        if(condition != null) {
            ConditionEvaluator<Condition> conditionEvaluator = evaluatorFactory.getEvaluator(condition);
            eval = conditionEvaluator.eval(condition);
        } else {
            LOG.debug("Reached the else statement, evaluating");
            eval = true;
        }

        if(eval) {
            LOG.debug("Condition: {} is true actions: {} will be executed", condition, actions);
            evalActions(actions);
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
        Set<String> dependentItems = new HashSet<>();
        input.getBranches().forEach(b -> {
            if(b.getCondition() != null) {
                ConditionEvaluator<Condition> conditionEvaluator = evaluatorFactory.getEvaluator(b.getCondition());
                dependentItems.addAll(conditionEvaluator.getDependentItems(b.getCondition()));
            }
        });


        return dependentItems;
    }
}
