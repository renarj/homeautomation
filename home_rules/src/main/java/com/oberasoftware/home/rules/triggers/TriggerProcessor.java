package com.oberasoftware.home.rules.triggers;

import com.oberasoftware.home.rules.api.general.Rule;
import com.oberasoftware.home.rules.api.trigger.Trigger;

/**
 * @author Renze de Vries
 */
public interface TriggerProcessor {
    void register(Trigger trigger, Rule rule);

    void remove(Trigger trigger, Rule rule);

    default void onStarted() {

    }

    default void onStopping() {

    }
}
