package com.oberasoftware.home.service.rules;


import com.oberasoftware.home.api.commands.Command;
import com.oberasoftware.home.api.events.DeviceEvent;

import java.util.List;
import java.util.function.Predicate;

/**
 * @author renarj
 */
public class Rule {

    private final List<Predicate<DeviceEvent>> predicates;
    private final List<ConditionSupplier> conditions;
    private final List<Command> actions;

    public Rule(List<Predicate<DeviceEvent>> predicates, List<ConditionSupplier> conditions, List<Command> actions) {
        this.predicates = predicates;
        this.conditions = conditions;
        this.actions = actions;
    }

    public boolean evaluate(DeviceEvent deviceEvent) {
        return predicates.stream().allMatch(p -> p.test(deviceEvent)) && conditions.stream().allMatch(ConditionSupplier::evaluate);
    }

    public List<Command> getActions() {
        return actions;
    }

    @Override
    public String toString() {
        return "Rule{" +
                "predicates=" + predicates +
                ", conditions=" + conditions +
                ", actions=" + actions +
                '}';
    }
}
