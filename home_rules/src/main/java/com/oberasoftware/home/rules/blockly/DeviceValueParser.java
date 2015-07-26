package com.oberasoftware.home.rules.blockly;

import com.oberasoftware.home.rules.api.ItemValue;
import com.oberasoftware.home.rules.api.ResolvableValue;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import org.w3c.dom.Element;

import static com.oberasoftware.home.rules.blockly.XMLUtils.findElementWithAttribute;
import static com.oberasoftware.home.rules.blockly.XMLUtils.findFieldElement;
import static com.oberasoftware.home.rules.blockly.XMLUtils.findFirstBlock;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Renze de Vries
 */
@Component
public class DeviceValueParser implements FragmentParser<ResolvableValue> {
    private static final Logger LOG = getLogger(DeviceValueParser.class);

    @Override
    public boolean supportsType(String type) {
        return "devicevalue".equals(type);
    }

    @Override
    public ResolvableValue parse(Element node) throws BlocklyParseException {
        Element labelField = findFieldElement(node, "label")
                .orElseThrow(() -> new BlocklyParseException("No label specified in device value"));
        String label = labelField.getTextContent();
        LOG.debug("Found device label criteria: {}", label);

        Element itemElement = findElementWithAttribute(node, "value", "name", "item")
                .orElseThrow(() -> new BlocklyParseException("No item specified"));
        Element itemBlock = findFirstBlock(itemElement)
                .orElseThrow(() -> new BlocklyParseException("No item specified"));
        String itemDescriptor = itemBlock.getAttribute("type");
        String itemId = itemDescriptor.substring(itemDescriptor.indexOf(".") + 1);
        LOG.debug("Found itemId: {}", itemId);

        return new ItemValue(itemId, label);
    }
}
