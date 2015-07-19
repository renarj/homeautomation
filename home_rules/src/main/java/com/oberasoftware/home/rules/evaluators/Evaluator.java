package com.oberasoftware.home.rules.evaluators;

/**
 * @author Renze de Vries
 */
public interface Evaluator<T, R> {
    R eval(T input);
}
