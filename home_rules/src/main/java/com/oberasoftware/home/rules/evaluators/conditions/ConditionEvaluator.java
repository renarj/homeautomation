package com.oberasoftware.home.rules.evaluators.conditions;

import com.oberasoftware.home.rules.api.Condition;
import com.oberasoftware.home.rules.evaluators.Evaluator;

import java.util.Set;

/**
 * @author Renze de Vries
 */
public interface ConditionEvaluator<T extends Condition> extends Evaluator<T, Boolean> {
    Set<String> getDependentItems(T input);
}
