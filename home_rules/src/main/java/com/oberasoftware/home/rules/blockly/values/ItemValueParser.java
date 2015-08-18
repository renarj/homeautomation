package com.oberasoftware.home.rules.blockly.values;

import com.oberasoftware.home.api.types.VALUE_TYPE;
import com.oberasoftware.home.rules.api.values.ItemValue;
import com.oberasoftware.home.rules.api.values.ResolvableValue;
import com.oberasoftware.home.rules.api.values.StaticValue;
import com.oberasoftware.home.rules.blockly.BlockParser;
import com.oberasoftware.home.rules.blockly.BlockParserFactory;
import com.oberasoftware.home.rules.blockly.BlocklyParseException;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Element;

import static com.oberasoftware.home.rules.blockly.XMLUtils.findElementWithAttribute;
import static com.oberasoftware.home.rules.blockly.XMLUtils.findFirstBlock;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Renze de Vries
 */
@Component
public class ItemValueParser implements BlockParser<ResolvableValue> {
    private static final Logger LOG = getLogger(ItemValueParser.class);

    @Autowired
    private BlockParserFactory blockParserFactory;

    @Override
    public boolean supportsType(String type) {
        return "getItemValue".equals(type);
    }

    @Override
    public ResolvableValue parse(Element node) throws BlocklyParseException {
        Element labelValue = findElementWithAttribute(node, "value", "name", "label")
                .orElseThrow(() -> new BlocklyParseException("No label specified in device value"));
        Element labelBlock = findFirstBlock(labelValue)
                .orElseThrow(() -> new BlocklyParseException("No label block defined"));
        String type = labelBlock.getAttribute("type");
        BlockParser<ResolvableValue> blockParser = blockParserFactory.getParser(type);
        String label = getLabelValue(blockParser.parse(labelBlock));
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

    public static String getLabelValue(ResolvableValue resolvableValue) throws BlocklyParseException {
        if(resolvableValue instanceof StaticValue) {
            StaticValue staticValue = (StaticValue) resolvableValue;
            if(staticValue.getType() == VALUE_TYPE.STRING) {
                return (String) staticValue.getValue();
            }
        }

        throw new BlocklyParseException("Unable to retrieve label value");
    }
}
