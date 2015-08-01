package com.oberasoftware.home.rules.blockly.values;

import com.oberasoftware.home.rules.blockly.BlockParser;
import com.oberasoftware.home.rules.blockly.BlocklyParseException;
import org.springframework.stereotype.Component;
import org.w3c.dom.Element;

import static com.oberasoftware.home.rules.blockly.XMLUtils.findFieldElement;

/**
 * @author Renze de Vries
 */
@Component
public class LabelBlockParser implements BlockParser<String> {
    @Override
    public boolean supportsType(String type) {
        return "label".equals(type) || "label_text".equalsIgnoreCase(type);
    }

    @Override
    public String parse(Element node) throws BlocklyParseException {
        Element labelElement = findFieldElement(node, "label")
                .orElseThrow(() -> new BlocklyParseException("Could not find label element"));

        return labelElement.getTextContent();
    }
}
