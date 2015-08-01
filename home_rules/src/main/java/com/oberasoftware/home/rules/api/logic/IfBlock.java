package com.oberasoftware.home.rules.api.logic;

import com.oberasoftware.home.rules.api.Block;

import java.util.List;

/**
 * @author Renze de Vries
 */
public class IfBlock implements Block {
    private List<IfBranch> branches;

    public IfBlock(List<IfBranch> branches) {
        this.branches = branches;
    }

    public IfBlock() {
    }

    public List<IfBranch> getBranches() {
        return branches;
    }

    public void setBranches(List<IfBranch> branches) {
        this.branches = branches;
    }

    @Override
    public String toString() {
        return "IfBlock{" +
                "branches=" + branches +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IfBlock ifBlock = (IfBlock) o;

        return !(branches != null ? !branches.equals(ifBlock.branches) : ifBlock.branches != null);

    }

    @Override
    public int hashCode() {
        return branches != null ? branches.hashCode() : 0;
    }
}
