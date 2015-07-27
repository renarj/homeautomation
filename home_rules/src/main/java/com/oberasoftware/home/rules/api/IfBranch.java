package com.oberasoftware.home.rules.api;

import java.util.List;

/**
 * @author Renze de Vries
 */
public class IfBranch implements Block {
    private Condition condition;
    private List<Action> actions;

    public IfBranch(Condition condition, List<Action> actions) {
        this.condition = condition;
        this.actions = actions;
    }

    public IfBranch() {
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public List<Action> getActions() {
        return actions;
    }

    public void setActions(List<Action> actions) {
        this.actions = actions;
    }
}
