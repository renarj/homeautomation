package com.oberasoftware.home.rules.blockly;

import com.google.common.collect.Iterables;
import com.google.common.io.CharSource;
import com.google.common.io.Resources;
import com.oberasoftware.home.api.commands.SwitchCommand;
import com.oberasoftware.home.api.types.VALUE_TYPE;
import com.oberasoftware.home.rules.RuleConfiguration;
import com.oberasoftware.home.rules.api.Action;
import com.oberasoftware.home.rules.api.CompareCondition;
import com.oberasoftware.home.rules.api.DeviceTrigger;
import com.oberasoftware.home.rules.api.IfBlock;
import com.oberasoftware.home.rules.api.IfBranch;
import com.oberasoftware.home.rules.api.ItemValue;
import com.oberasoftware.home.rules.api.Operator;
import com.oberasoftware.home.rules.api.Rule;
import com.oberasoftware.home.rules.api.StaticValue;
import com.oberasoftware.home.rules.api.SwitchAction;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.nio.charset.Charset;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

/**
 * @author Renze de Vries
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RuleConfiguration.class)
public class BlocklyParserTest {

    @Autowired
    private BlocklyParser blocklyParser;

    @Test
    public void testParseSimpleRule() throws Exception {
        CharSource s = Resources.asCharSource(this.getClass().getResource("/simple_luminance_rule.xml"), Charset.defaultCharset());
        String blocklyRuleXML = s.read();

        Rule rule = blocklyParser.toRule(blocklyRuleXML);
        assertThat(rule, notNullValue());
        assertThat(rule.getName(), is("Activate Lights After Dark"));

        assertThat(rule.getTrigger(), notNullValue());
        assertThat(rule.getTrigger() instanceof DeviceTrigger, is(true));


        assertThat(rule.getBlock(), notNullValue());
        assertThat(rule.getBlock() instanceof IfBlock, is(true));

        IfBlock ifBlock = (IfBlock) rule.getBlock();
        assertThat(ifBlock.getBranches().size(), is(1));

        IfBranch firstBranch = Iterables.getFirst(ifBlock.getBranches(), null);
        assertThat(firstBranch, notNullValue());

        List<Action> actions = firstBranch.getActions();
        assertThat(actions.size(), is(1));

        assertThat(firstBranch.getCondition(), notNullValue());
        assertThat(firstBranch.getCondition() instanceof CompareCondition, is(true));

        CompareCondition compareCondition = (CompareCondition) firstBranch.getCondition();
        assertThat(compareCondition.getLeftValue(), notNullValue());
        assertThat(compareCondition.getRightValue(), notNullValue());
        assertThat(compareCondition.getOperator(), is(Operator.SMALLER_THAN_EQUALS));

        assertThat(compareCondition.getLeftValue() instanceof ItemValue, is(true));
        ItemValue itemValue = (ItemValue) compareCondition.getLeftValue();
        assertThat(itemValue.getItemId(), is("75f1c663-b7df-4036-8a2d-5e3d95f6a596"));
        assertThat(itemValue.getLabel(), is("luminance"));

        assertThat(compareCondition.getRightValue() instanceof StaticValue, is(true));
        StaticValue staticValue = (StaticValue) compareCondition.getRightValue();
        assertThat(staticValue.getType(), is(VALUE_TYPE.NUMBER));
        assertThat(staticValue.getValue(), is(10l));

        Action action = Iterables.getFirst(actions, null);
        assertThat(action, notNullValue());
        assertThat(action instanceof SwitchAction, is(true));

        SwitchAction switchAction = (SwitchAction) action;
        assertThat(switchAction.getItemId(), is("0000001e-0661-7a39-0000-014e5fa2231e"));
        assertThat(switchAction.getState(), is(SwitchCommand.STATE.ON));
    }

    @Test
    public void testParseIfElse() throws Exception {
        CharSource s = Resources.asCharSource(this.getClass().getResource("/simple_ifelse_rule.xml"), Charset.defaultCharset());
        String blocklyRuleXML = s.read();

        Rule rule = blocklyParser.toRule(blocklyRuleXML);

        assertThat(rule.getBlock(), notNullValue());
        assertThat(rule.getBlock() instanceof IfBlock, is(true));

        IfBlock ifBlock = (IfBlock) rule.getBlock();
        assertThat(ifBlock.getBranches().size(), is(2));

        List<IfBranch> branches = ifBlock.getBranches();
        IfBranch conditionBranch = branches.get(0);
        IfBranch elseBranch = branches.get(1);

        assertThat(conditionBranch, notNullValue());
        assertThat(elseBranch, notNullValue());

        assertThat(conditionBranch.getCondition(), notNullValue());
        assertThat(elseBranch.getCondition(), nullValue());
    }
}
