package com.oberasoftware.home.rules.blockly;

import com.oberasoftware.home.api.commands.SwitchCommand;
import com.oberasoftware.home.rules.RuleConfiguration;
import com.oberasoftware.home.rules.api.Operator;
import com.oberasoftware.home.rules.api.general.Rule;
import com.oberasoftware.home.rules.api.general.SwitchItem;
import com.oberasoftware.home.rules.builder.ConditionBuilder;
import com.oberasoftware.home.rules.builder.RuleBuilder;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static com.oberasoftware.home.rules.blockly.BlocklyHelper.parseRule;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author Renze de Vries
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RuleConfiguration.class)
public class BlocklyParserTest {

    @Autowired
    private BlocklyParser blocklyParser;

    @org.junit.Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testParseSimpleRule() throws Exception {
        Rule rule = parseRule(blocklyParser, "/simple_luminance_rule.xml");

        Rule expectedRule = RuleBuilder.create("Activate Lights After Dark")
                .triggerOnDeviceChange()
                .when(ConditionBuilder.create()
                        .itemValue("75f1c663-b7df-4036-8a2d-5e3d95f6a596", "luminance")
                        .compare(Operator.SMALLER_THAN, 10))
                .thenDo(new SwitchItem("0000001e-0661-7a39-0000-014e5fa2231e", SwitchCommand.STATE.ON))
                .build();

        assertRule(rule, expectedRule);
    }

    @Test
    public void testParseIfElse() throws Exception {
        Rule rule = parseRule(blocklyParser, "/simple_ifelse_rule.xml");

        Rule expectedRule = RuleBuilder.create("testRule")
                .triggerOnDeviceChange()
                .when(ConditionBuilder.create()
                        .itemValue("4407eace-7bb1-43ac-aefd-da4cae9fc97a", "temperature")
                        .compare(Operator.SMALLER_THAN_EQUALS, 20l))
                .thenDo(new SwitchItem("486fd173-b3ea-417f-b46c-e7d3579f59e1", SwitchCommand.STATE.ON))
                .orElseDo(new SwitchItem("486fd173-b3ea-417f-b46c-e7d3579f59e1", SwitchCommand.STATE.OFF)).build();
        assertRule(rule, expectedRule);
    }

    @Test
    public void testParseComplexRule() throws Exception {
        Rule rule = parseRule(blocklyParser, "/moveafterlight_rule.xml");

        Rule expectedRule = RuleBuilder.create("Light off After Dark and No Movement")
                .triggerOnDeviceChange()
                    .when(ConditionBuilder.create().and(
                            ConditionBuilder.create()
                                    .itemValue("1a950cf2-b721-418b-8744-11b7d1c476ca", "luminance")
                                    .compare(Operator.SMALLER_THAN_EQUALS, 10)
                            , ConditionBuilder.create().itemValue("1a950cf2-b721-418b-8744-11b7d1c476ca", "on-off")
                                    .compare(Operator.EQUALS, "on")
                    )).thenDo(
                        new SwitchItem("28da1433-f601-4b8d-a0f9-7dae61e83ad9", SwitchCommand.STATE.ON),
                        new SwitchItem("0000001e-0661-7a39-0000-014e5fa2231f", SwitchCommand.STATE.ON))
                    .orElseDo(new SwitchItem("0000001e-0661-7a39-0000-014e5fa2232b", SwitchCommand.STATE.OFF))
                .build();

        assertRule(rule, expectedRule);
    }

    @Test
    public void testParseStartValue() throws Exception {
        Rule actual = parseRule(blocklyParser, "/update_power_start.xml");

        Rule expectedRule = RuleBuilder.create("Update Power Start")
                .triggerAtTime(0, 0)
                .triggerOnSystemChange()
                .setItemState("6d1a20a5-7347-41cf-bdc7-4f6df2035b24", "PowerStart")
                .fromItem("6d1a20a5-7347-41cf-bdc7-4f6df2035b24", "KWH")
                .build();
        assertRule(actual, expectedRule);
    }

    @Test
    public void testParseMath() throws Exception {
        Rule actual = parseRule(blocklyParser, "/simple_math.xml");


    }


    private void assertRule(Rule actual, Rule expected) {
        assertThat(actual.getId(), is(expected.getId()));
        assertThat(actual.getName(), is(expected.getName()));
        assertThat(actual.getBlock(), is(expected.getBlock()));
        assertThat(actual.getTriggers(), is(expected.getTriggers()));
    }
}
