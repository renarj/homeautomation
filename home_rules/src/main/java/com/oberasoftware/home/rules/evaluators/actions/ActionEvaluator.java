package com.oberasoftware.home.rules.evaluators.actions;

import com.oberasoftware.home.rules.api.Action;
import com.oberasoftware.home.rules.evaluators.Evaluator;

/**
 * @author Renze de Vries
 */
public interface ActionEvaluator<T extends Action> extends Evaluator<T, Boolean> {
}
