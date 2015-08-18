package com.oberasoftware.home.rules.evaluators;

import com.oberasoftware.home.api.exceptions.RuntimeHomeAutomationException;

/**
 * @author Renze de Vries
 */
public class EvalException extends RuntimeHomeAutomationException {
    public EvalException(String message, Throwable e) {
        super(message, e);
    }

    public EvalException(String message) {
        super(message);
    }
}
