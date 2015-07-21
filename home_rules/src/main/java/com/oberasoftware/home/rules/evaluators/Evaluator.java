package com.oberasoftware.home.rules.evaluators;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Renze de Vries
 */
public interface Evaluator<T, R> {
    R eval(T input);

    default Set<String> getDependentItems(T input) {
        return new HashSet<>();
    }
}
