package com.oberasoftware.home.rules;

import com.oberasoftware.home.api.exceptions.HomeAutomationException;
import com.oberasoftware.home.rules.api.general.Rule;

/**
 * @author Renze de Vries
 */
public interface RuleEngine {
    void register(Rule rule) throws HomeAutomationException;

    void evalRule(String id);

    void removeRule(String id);

    void onStarted();

    void onStopping();
}
