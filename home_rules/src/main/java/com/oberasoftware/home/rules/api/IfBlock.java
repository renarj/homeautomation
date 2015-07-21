package com.oberasoftware.home.rules.api;

import java.util.List;

/**
 * @author Renze de Vries
 */
public class IfBlock implements Block {
    private Condition condition;
    private List<Action> actions;

    public IfBlock(Condition condition, List<Action> actions) {
        this.condition = condition;
        this.actions = actions;
    }

    public IfBlock() {
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
