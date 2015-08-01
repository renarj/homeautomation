package com.oberasoftware.home.rules.evaluators.blocks;

import com.oberasoftware.home.api.AutomationBus;
import com.oberasoftware.home.api.commands.DeviceCommand;
import com.oberasoftware.home.api.events.devices.DeviceCommandEvent;
import com.oberasoftware.home.api.types.Value;
import com.oberasoftware.home.core.commands.ValueCommandImpl;
import com.oberasoftware.home.rules.api.general.SetState;
import com.oberasoftware.home.rules.api.values.ItemValue;
import com.oberasoftware.home.rules.api.values.ResolvableValue;
import com.oberasoftware.home.rules.evaluators.EvaluatorFactory;
import com.oberasoftware.home.rules.evaluators.values.ValueEvaluator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Renze de Vries
 */
@Component
public class SetStateEvaluator implements BlockEvaluator<SetState> {

    @Autowired
    private AutomationBus automationBus;

    @Autowired
    private EvaluatorFactory evaluatorFactory;


    @Override
    public Boolean eval(SetState input) {
        ItemValue targetItem = input.getItemValue();

        ValueEvaluator<ResolvableValue> valueEvaluator = evaluatorFactory.getEvaluator(input.getResolvableValue());
        Map<String, Value> values = new HashMap<>();
        values.put(targetItem.getLabel(), valueEvaluator.eval(input.getResolvableValue()));

        DeviceCommand deviceCommand = new ValueCommandImpl(targetItem.getItemId(), values);

        automationBus.publish(new DeviceCommandEvent(targetItem.getItemId(), deviceCommand));

        return true;
    }
}
