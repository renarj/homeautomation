package com.oberasoftware.home.rules.blockly.values;

import com.oberasoftware.home.api.types.VALUE_TYPE;
import com.oberasoftware.home.rules.api.values.ResolvableValue;
import com.oberasoftware.home.rules.api.values.StaticValue;
import com.oberasoftware.home.rules.blockly.BlockParser;
import com.oberasoftware.home.rules.blockly.BlocklyParseException;
import org.springframework.stereotype.Component;
import org.w3c.dom.Element;

import static com.oberasoftware.home.rules.blockly.XMLUtils.findFieldElement;

/**
 * @author Renze de Vries
 */
@Component
public class LabelBlockParser implements BlockParser<ResolvableValue> {
    @Override
    public boolean supportsType(String type) {
        return "label".equals(type) || "label_text".equalsIgnoreCase(type);
    }

    @Override
    public ResolvableValue parse(Element node) throws BlocklyParseException {
        Element labelElement = findFieldElement(node, "label")
                .orElseThrow(() -> new BlocklyParseException("Could not find label element"));

        String labelText = labelElement.getTextContent();
        if(labelText.equals("movement")) {
            //we do this as movement is not an official label in haas
            labelText = "on-off";
        }

        return new StaticValue(labelText, VALUE_TYPE.STRING);
    }
}
