package com.oberasoftware.home.rules.blockly;

import com.oberasoftware.home.rules.api.DeviceTrigger;
import com.oberasoftware.home.rules.api.Trigger;
import org.springframework.stereotype.Component;
import org.w3c.dom.Element;

import java.util.Optional;

import static com.oberasoftware.home.rules.blockly.XMLConstants.NAME_ATTRIBUTE;
import static com.oberasoftware.home.rules.blockly.XMLUtils.findElementWithAttribute;

/**
 * @author Renze de Vries
 */
@Component
public class TriggerParser implements FragmentParser<Trigger> {

    @Override
    public boolean supportsType(String type) {
        return "triggerDevice".equals(type);
    }

    @Override
    public Trigger parse(Element node) throws BlocklyParseException {
        Optional<Element> triggerElement = findElementWithAttribute(node, "statement", NAME_ATTRIBUTE, "ruleTrigger");
        if(triggerElement.isPresent()) {
            Optional<Element> triggerBlock = XMLUtils.findFirstBlock(triggerElement.get());
            if(triggerBlock.isPresent()) {
                String triggerType = triggerBlock.get().getAttribute("type");

                switch (triggerType) {
                    case "triggerDevice":
                        return new DeviceTrigger(DeviceTrigger.TRIGGER_TYPE.DEVICE_STATE_CHANGE);
                    default:
                        throw new BlocklyParseException("Trigger type: " + triggerType + " not supported");

                }
            }
        }

        throw new BlocklyParseException("No trigger was defined for rule");
    }
}
