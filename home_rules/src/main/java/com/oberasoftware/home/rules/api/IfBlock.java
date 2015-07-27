package com.oberasoftware.home.rules.api;

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
}
