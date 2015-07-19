package com.oberasoftware.home.rules.api;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * @author Renze de Vries
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "type")
public class Rule {

    private String name;
    private Condition condition;
    private Action action;
    private Trigger trigger;

    public Rule(String name, Condition condition, Action action, Trigger trigger) {
        this.name = name;
        this.condition = condition;
        this.action = action;
        this.trigger = trigger;
    }

    public Rule() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public Trigger getTrigger() {
        return trigger;
    }

    public void setTrigger(Trigger trigger) {
        this.trigger = trigger;
    }

    @Override
    public String toString() {
        return "RuleImpl{" +
                "name='" + name + '\'' +
                ", condition=" + condition +
                ", statement=" + action +
                '}';
    }
}
