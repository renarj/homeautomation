package com.oberasoftware.home.rules.triggers;

import com.oberasoftware.home.rules.RuleEngine;
import com.oberasoftware.home.rules.api.general.Rule;
import com.oberasoftware.home.rules.api.trigger.SystemTrigger;
import com.oberasoftware.home.rules.api.trigger.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Renze de Vries
 */
@Component
public class SystemTriggerProcessor implements TriggerProcessor {

    private List<Rule> systemRules = new CopyOnWriteArrayList<>();

    @Autowired
    private RuleEngine ruleEngine;

    @Override
    public void register(Trigger trigger, Rule rule) {
        if(trigger instanceof SystemTrigger) {
            systemRules.add(rule);
        }
    }

    @Override
    public void remove(Trigger trigger, Rule rule) {
        if(trigger instanceof SystemTrigger) {
            systemRules.remove(rule);
        }
    }

    @Override
    public void onStarted() {
        evalRules();
    }

    @Override
    public void onStopping() {
        evalRules();
    }

    private void evalRules() {
        systemRules.forEach(r -> ruleEngine.evalRule(r.getId()));
    }
}
