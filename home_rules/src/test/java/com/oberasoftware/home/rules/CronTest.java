package com.oberasoftware.home.rules;

import com.google.common.util.concurrent.Uninterruptibles;
import com.oberasoftware.home.rules.api.general.Rule;
import com.oberasoftware.home.rules.api.trigger.CronTrigger;
import com.oberasoftware.home.rules.triggers.CronTriggerProcessor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * @author Renze de Vries
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {RuleConfiguration.class, TestConfiguration.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class CronTest {

    @Autowired
    private CronTriggerProcessor cronTriggerProcessor;

    @Test
    public void testSchedule() {
        cronTriggerProcessor.register(new CronTrigger("0 0/1 * 1/1 * ? *"), new Rule("X", null, null, new ArrayList<>()));


        Uninterruptibles.sleepUninterruptibly(1, TimeUnit.HOURS);
    }
}
