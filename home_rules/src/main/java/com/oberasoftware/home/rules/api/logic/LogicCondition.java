package com.oberasoftware.home.rules.api.logic;

import com.oberasoftware.home.rules.api.Condition;

import java.util.List;

/**
 * @author Renze de Vries
 */
public class LogicCondition implements Condition {
    public enum TYPE {
        AND,
        OR
    }

    private TYPE type;
    private List<Condition> conditions;

    public LogicCondition(TYPE type, List<Condition> conditions) {
        this.type = type;
        this.conditions = conditions;
    }

    public LogicCondition() {
    }

    public TYPE getType() {
        return type;
    }

    public void setType(TYPE type) {
        this.type = type;
    }

    public List<Condition> getConditions() {
        return conditions;
    }

    public void setConditions(List<Condition> conditions) {
        this.conditions = conditions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LogicCondition that = (LogicCondition) o;

        if (type != that.type) return false;
        return conditions.equals(that.conditions);
    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + conditions.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "LogicCondition{" +
                "type=" + type +
                ", conditions=" + conditions +
                '}';
    }
}
