package com.oberasoftware.home.rules.api.logic;

import com.oberasoftware.home.rules.api.Block;
import com.oberasoftware.home.rules.api.Condition;

import java.util.List;

/**
 * @author Renze de Vries
 */
public class IfBranch implements Block {
    private Condition condition;
    private List<Block> statements;

    public IfBranch(Condition condition, List<Block> statements) {
        this.condition = condition;
        this.statements = statements;
    }

    public IfBranch() {
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public List<Block> getStatements() {
        return statements;
    }

    public void setStatements(List<Block> statements) {
        this.statements = statements;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IfBranch branch = (IfBranch) o;

        if (condition != null ? !condition.equals(branch.condition) : branch.condition != null) return false;
        return statements.equals(branch.statements);

    }

    @Override
    public int hashCode() {
        int result = condition != null ? condition.hashCode() : 0;
        result = 31 * result + statements.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "IfBranch{" +
                "condition=" + condition +
                ", statements=" + statements +
                '}';
    }
}
