package com.oberasoftware.home.rules.builder;

import com.oberasoftware.home.rules.api.Block;
import com.oberasoftware.home.rules.api.general.CompositeBlock;
import com.oberasoftware.home.rules.api.general.Rule;
import com.oberasoftware.home.rules.api.trigger.DayTimeTrigger;
import com.oberasoftware.home.rules.api.trigger.DeviceTrigger;
import com.oberasoftware.home.rules.api.trigger.SystemTrigger;
import com.oberasoftware.home.rules.api.trigger.Trigger;
import com.oberasoftware.home.rules.api.values.ItemValue;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Renze de Vries
 */
public class RuleBuilder {

    private Rule rule;

    private List<Trigger> triggers = new ArrayList<>();

    private List<BlockBuilder> blockBuilders = new ArrayList<>();

    private RuleBuilder(String ruleName) {
        this.rule = new Rule(null, ruleName, null, null);
    }

    public static RuleBuilder create(String name) {
        return new RuleBuilder(name);
    }

    public RuleBuilder triggerOnDeviceChange() {
        this.triggers.add(new DeviceTrigger(DeviceTrigger.TRIGGER_TYPE.DEVICE_STATE_CHANGE));

        return this;
    }

    public RuleBuilder triggerAtTime(int hour, int minute) {
        this.triggers.add(new DayTimeTrigger(hour, minute));

        return this;
    }

    public RuleBuilder triggerOnSystemChange() {
        this.triggers.add(new SystemTrigger());

        return this;
    }

    public IfBlockBuilder when(ConditionBuilder conditionBuilder) {
        IfBlockBuilder ifBlockBuilder = new IfBlockBuilder(this, conditionBuilder);
        blockBuilders.add(ifBlockBuilder);

        return ifBlockBuilder;
    }

    public SetStateBlockBuilder setItemState(String itemId, String label) {
        SetStateBlockBuilder setStateBlockBuilder = new SetStateBlockBuilder(this, new ItemValue(itemId, label));

        blockBuilders.add(setStateBlockBuilder);

        return setStateBlockBuilder;
    }

    public Rule build() {
        rule.setTriggers(triggers);

        List<Block> blocks = blockBuilders.stream().map(BlockBuilder::buildBlock).collect(Collectors.toList());

        if(blocks.size() > 1) {
            rule.setBlock(new CompositeBlock(blocks));
        } else {
            rule.setBlock(blocks.stream().findFirst().get());
        }

        return rule;
    }
}
