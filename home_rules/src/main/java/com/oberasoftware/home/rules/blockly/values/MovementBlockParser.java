package com.oberasoftware.home.rules.blockly.values;

import com.oberasoftware.home.api.types.VALUE_TYPE;
import com.oberasoftware.home.rules.api.values.ResolvableValue;
import com.oberasoftware.home.rules.api.values.StaticValue;
import com.oberasoftware.home.rules.blockly.BlockParser;
import com.oberasoftware.home.rules.blockly.BlocklyParseException;
import com.oberasoftware.home.rules.blockly.XMLUtils;
import org.springframework.stereotype.Component;
import org.w3c.dom.Element;

/**
 * @author Renze de Vries
 */
@Component
public class MovementBlockParser implements BlockParser<ResolvableValue> {
    @Override
    public boolean supportsType(String type) {
        return "movement".equalsIgnoreCase(type);
    }

    @Override
    public ResolvableValue parse(Element node) throws BlocklyParseException {
        Element movementCondition = XMLUtils.findFieldElement(node, "NAME")
                .orElseThrow(() -> new BlocklyParseException("No movement condition specified"));
        String moveCondition = movementCondition.getTextContent();
        if(moveCondition.equalsIgnoreCase("detected")) {
            return new StaticValue("on", VALUE_TYPE.STRING);
        } else {
            return new StaticValue("off", VALUE_TYPE.STRING);
        }
    }
}
