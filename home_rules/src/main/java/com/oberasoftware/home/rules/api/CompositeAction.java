package com.oberasoftware.home.rules.api;

import java.util.List;

/**
 * @author Renze de Vries
 */
public interface CompositeAction extends Action {
    List<Action> getStatements();
}
