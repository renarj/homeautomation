package com.oberasoftware.home.rules.builder;

import com.oberasoftware.home.rules.api.Block;
import com.oberasoftware.home.rules.api.general.SetState;
import com.oberasoftware.home.rules.api.values.ItemValue;
import com.oberasoftware.home.rules.api.values.ResolvableValue;

/**
 * @author Renze de Vries
 */
public class SetStateBlockBuilder implements BlockBuilder {

    private RuleBuilder ruleBuilder;
    private ItemValue itemValue;
    private ResolvableValue sourceValue;

    public SetStateBlockBuilder(RuleBuilder ruleBuilder, ItemValue itemValue) {
        this.ruleBuilder = ruleBuilder;
        this.itemValue = itemValue;
    }

    public RuleBuilder fromItem(String itemId, String label) {
        sourceValue = new ItemValue(itemId, label);

        return ruleBuilder;
    }

    @Override
    public Block buildBlock() {
        return new SetState(itemValue, sourceValue);
    }
}
