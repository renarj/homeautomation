package com.oberasoftware.home.service.rules;

import com.oberasoftware.base.event.EventHandler;
import com.oberasoftware.base.event.EventSubscribe;
import com.oberasoftware.home.api.AutomationBus;
import com.oberasoftware.home.api.events.DeviceEvent;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author renarj
 */
@Component
public class RuleEngine implements EventHandler {
    private static final Logger LOG = getLogger(RuleEngine.class);

    private List<Rule> rules = new ArrayList<>();

    private Lock lock = new ReentrantLock();

    @Autowired
    private AutomationBus eventBus;

    public void add(ConditionBuilder conditionBuilder) {
        lock.lock();
        try {
            rules.add(conditionBuilder.build());
        } finally {
            lock.unlock();
        }
    }

    private List<Rule> copy() {
        lock.lock();
        try {
            return new ArrayList<>(rules);
        } finally {
            lock.unlock();
        }
    }

    @EventSubscribe
    public void receive(DeviceEvent event) throws Exception {
        List<Rule> rulesCopy = copy();
        LOG.debug("Received device event: {} checking: {} rules", event, rulesCopy.size());
        rulesCopy.forEach(r -> {
            if(r.evaluate(event)) {
                LOG.debug("Rule evaluated to true, activating actions: {}", r);
                doDeviceActions(r);
            }
        });
    }

    private void doDeviceActions(Rule rule) {
        rule.getActions().forEach(a -> {
            LOG.debug("Doing device action: {} for rule: {}", a, rule);

            eventBus.publish(a);
        });
    }
}
