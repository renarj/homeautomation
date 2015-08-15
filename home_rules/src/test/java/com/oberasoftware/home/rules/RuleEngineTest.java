package com.oberasoftware.home.rules;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.oberasoftware.base.event.Event;
import com.oberasoftware.home.api.commands.SwitchCommand;
import com.oberasoftware.home.api.events.devices.ItemCommandEvent;
import com.oberasoftware.home.api.exceptions.HomeAutomationException;
import com.oberasoftware.home.api.model.Status;
import com.oberasoftware.home.api.types.VALUE_TYPE;
import com.oberasoftware.home.core.model.StateImpl;
import com.oberasoftware.home.core.model.StateItemImpl;
import com.oberasoftware.home.core.types.ValueImpl;
import com.oberasoftware.home.rules.api.logic.CompareCondition;
import com.oberasoftware.home.rules.api.Condition;
import com.oberasoftware.home.rules.api.trigger.DeviceTrigger;
import com.oberasoftware.home.rules.api.logic.IfBlock;
import com.oberasoftware.home.rules.api.logic.IfBranch;
import com.oberasoftware.home.rules.api.values.ItemValue;
import com.oberasoftware.home.rules.api.Operator;
import com.oberasoftware.home.rules.api.general.Rule;
import com.oberasoftware.home.rules.api.values.StaticValue;
import com.oberasoftware.home.rules.api.general.SwitchItem;
import com.oberasoftware.home.rules.api.trigger.Trigger;
import com.oberasoftware.home.rules.test.MockAutomationBus;
import com.oberasoftware.home.rules.test.MockStateManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.StringWriter;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.UUID.randomUUID;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Renze de Vries
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {RuleConfiguration.class, TestConfiguration.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class RuleEngineTest {
    private static final Logger LOG = getLogger(RuleEngineTest.class);

    private static final String MY_ITEM_ID = "ff8daadb-5a35-4e39-ad99-5eefe73a18a7";
    private static final String LUMINANCE_LABEL = "Luminance";
    private static final String SWITCHABLE_DEVICE_ID = "SwitchableDeviceId";

    @Autowired
    private RuleEngine ruleEngine;

    @Autowired
    private MockAutomationBus mockAutomationBus;

    @Autowired
    private MockStateManager mockStateManager;

    @Test
    public void testSerialise() throws Exception {
        Trigger trigger = new DeviceTrigger(DeviceTrigger.TRIGGER_TYPE.DEVICE_STATE_CHANGE);
        Condition condition = new CompareCondition(
                new ItemValue(MY_ITEM_ID, LUMINANCE_LABEL),
                Operator.SMALLER_THAN_EQUALS,
                new StaticValue(10l, VALUE_TYPE.NUMBER));

        IfBranch branch = new IfBranch(condition, newArrayList(new SwitchItem(SWITCHABLE_DEVICE_ID, SwitchCommand.STATE.ON)));

        String ruleId = randomUUID().toString();
        Rule rule = new Rule(ruleId, "Light after dark", new IfBlock(newArrayList(branch)), Lists.newArrayList(trigger));

        StringWriter stringWriter = new StringWriter();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(stringWriter, rule);

        String json = stringWriter.toString();

        LOG.debug("Rule JSON: {}", json);


        Rule readRule = objectMapper.readValue(json, Rule.class);

        StringWriter secondWriter = new StringWriter();
        objectMapper.writeValue(secondWriter, readRule);
        assertThat(secondWriter.toString(), is(json));
    }

    @Test
    public void testEvaluate() throws HomeAutomationException {
        Trigger trigger = new DeviceTrigger(DeviceTrigger.TRIGGER_TYPE.DEVICE_STATE_CHANGE);

        Condition condition = new CompareCondition(
                new ItemValue(MY_ITEM_ID, LUMINANCE_LABEL),
                Operator.SMALLER_THAN_EQUALS,
                new StaticValue(10l, VALUE_TYPE.NUMBER));

        IfBranch branch = new IfBranch(condition, newArrayList(new SwitchItem(SWITCHABLE_DEVICE_ID, SwitchCommand.STATE.ON)));

        String ruleId = randomUUID().toString();
        Rule rule = new Rule(ruleId, "Light after dark", new IfBlock(newArrayList(branch)), Lists.newArrayList(trigger));

        StateImpl itemState = new StateImpl(MY_ITEM_ID, Status.ACTIVE);
        itemState.updateIfChanged(LUMINANCE_LABEL, new StateItemImpl(LUMINANCE_LABEL, new ValueImpl(VALUE_TYPE.NUMBER, 1l)));
        mockStateManager.addState(itemState);

        ruleEngine.register(rule);


        ruleEngine.evalRule(ruleId);

        List<Event> publishedEvents = mockAutomationBus.getPublishedEvents();
        assertThat(publishedEvents.size(), is(1));

        Event event = publishedEvents.get(0);
        assertThat(event instanceof ItemCommandEvent, is(true));
        ItemCommandEvent switchCommand = (ItemCommandEvent) event;
        assertThat(switchCommand.getItemId(), is(SWITCHABLE_DEVICE_ID));
        assertThat(((SwitchCommand)switchCommand.getCommand()).getState(), is(SwitchCommand.STATE.ON));
    }

    @Test
    public void testDeviceMovement() throws  Exception {
        Trigger trigger = new DeviceTrigger(DeviceTrigger.TRIGGER_TYPE.DEVICE_STATE_CHANGE);

        Condition condition = new CompareCondition(
                new ItemValue(MY_ITEM_ID, "on-off"),
                Operator.EQUALS,
                new StaticValue("on", VALUE_TYPE.STRING));

        IfBranch branch = new IfBranch(condition, newArrayList(new SwitchItem("LightId", SwitchCommand.STATE.ON)));
        String ruleId = randomUUID().toString();
        Rule rule = new Rule(ruleId, "Light on with movement", new IfBlock(newArrayList(branch)), Lists.newArrayList(trigger));

        StringWriter stringWriter = new StringWriter();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(stringWriter, rule);

        String json = stringWriter.toString();

        LOG.debug("Rule JSON: {}", json);



        StateImpl itemState = new StateImpl(MY_ITEM_ID, Status.ACTIVE);
        itemState.updateIfChanged("on-off", new StateItemImpl("on-off", new ValueImpl(VALUE_TYPE.STRING, "on")));
        mockStateManager.addState(itemState);

        ruleEngine.register(rule);

        ruleEngine.evalRule(ruleId);

        List<Event> publishedEvents = mockAutomationBus.getPublishedEvents();
        assertThat(publishedEvents.size(), is(1));

        Event event = publishedEvents.get(0);
        assertThat(event instanceof ItemCommandEvent, is(true));
        ItemCommandEvent switchCommand = (ItemCommandEvent) event;
        assertThat(switchCommand.getItemId(), is("LightId"));
        assertThat(((SwitchCommand)switchCommand.getCommand()).getState(), is(SwitchCommand.STATE.ON));

    }

}
