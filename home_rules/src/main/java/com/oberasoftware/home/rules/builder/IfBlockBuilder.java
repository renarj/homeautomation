package com.oberasoftware.home.rules.builder;

import com.oberasoftware.home.rules.api.Block;
import com.oberasoftware.home.rules.api.general.Rule;
import com.oberasoftware.home.rules.api.logic.IfBlock;
import com.oberasoftware.home.rules.api.logic.IfBranch;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

/**
 * @author Renze de Vries
 */
public class IfBlockBuilder implements BlockBuilder {

    private final RuleBuilder ruleBuilder;

    private List<IfBranch> branches = new ArrayList<>();

    private ConditionBuilder lastConditionBuilder;

    public IfBlockBuilder(RuleBuilder ruleBuilder, ConditionBuilder conditionBuilder) {
        this.ruleBuilder = ruleBuilder;
        this.lastConditionBuilder = conditionBuilder;
    }

    public IfBlockBuilder thenDo(Block... statements) {
        branches.add(new IfBranch(lastConditionBuilder.build(), asList(statements)));
        return this;
    }

    public IfBlockBuilder orElseIf(ConditionBuilder conditionBuilder) {
        lastConditionBuilder = conditionBuilder;
        return this;
    }

    public IfBlockBuilder orElseDo(Block... statements) {
        branches.add(new IfBranch(null, asList(statements)));
        return this;
    }

    @Override
    public Block buildBlock() {
        return new IfBlock(branches);
    }

    public Rule build() {
        return ruleBuilder.build();
    }
}
