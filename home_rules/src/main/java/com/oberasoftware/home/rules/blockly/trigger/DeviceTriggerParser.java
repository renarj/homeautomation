package com.oberasoftware.home.rules.blockly.trigger;

import com.oberasoftware.home.rules.api.trigger.DeviceTrigger;
import com.oberasoftware.home.rules.api.trigger.Trigger;
import com.oberasoftware.home.rules.blockly.BlockParser;
import com.oberasoftware.home.rules.blockly.BlocklyParseException;
import org.springframework.stereotype.Component;
import org.w3c.dom.Element;

/**
 * @author Renze de Vries
 */
@Component
public class DeviceTriggerParser implements BlockParser<Trigger> {

    @Override
    public boolean supportsType(String type) {
        return "deviceTrigger".equals(type);
    }

    @Override
    public Trigger parse(Element node) throws BlocklyParseException {
        return new DeviceTrigger(DeviceTrigger.TRIGGER_TYPE.DEVICE_STATE_CHANGE);
    }
}
