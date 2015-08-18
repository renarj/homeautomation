package com.oberasoftware.home.rules.blockly.general;

import com.oberasoftware.home.rules.api.general.SetState;
import com.oberasoftware.home.rules.api.values.ItemValue;
import com.oberasoftware.home.rules.api.values.ResolvableValue;
import com.oberasoftware.home.rules.blockly.BlockParser;
import com.oberasoftware.home.rules.blockly.BlockParserFactory;
import com.oberasoftware.home.rules.blockly.BlocklyParseException;
import com.oberasoftware.home.rules.blockly.values.ItemValueParser;
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
public class SetItemValueParser implements BlockParser<SetState> {
    private static final Logger LOG = getLogger(SetItemValueParser.class);

    @Autowired
    private BlockParserFactory blockParserFactory;

    @Override
    public boolean supportsType(String type) {
        return "setItemValue".equals(type);
    }

    @Override
    public SetState parse(Element node) throws BlocklyParseException {
        Element labelValue = findElementWithAttribute(node, "value", "name", "label")
                .orElseThrow(() -> new BlocklyParseException("No label specified in device value"));
        Element labelBlock = findFirstBlock(labelValue)
                .orElseThrow(() -> new BlocklyParseException("No label block defined"));
        String type = labelBlock.getAttribute("type");
        BlockParser<ResolvableValue> blockParser = blockParserFactory.getParser(type);
        String label = ItemValueParser.getLabelValue(blockParser.parse(labelBlock));
        LOG.debug("Found device label criteria: {}", label);

        Element itemElement = findElementWithAttribute(node, "value", "name", "item")
                .orElseThrow(() -> new BlocklyParseException("No item specified"));
        Element itemBlock = findFirstBlock(itemElement)
                .orElseThrow(() -> new BlocklyParseException("No item specified"));
        String itemDescriptor = itemBlock.getAttribute("type");
        String itemId = itemDescriptor.substring(itemDescriptor.indexOf(".") + 1);
        LOG.debug("Found itemId: {}", itemId);


        Element valueElement = findElementWithAttribute(node, "value", "name", "value")
                .orElseThrow(() -> new BlocklyParseException("No value specified for setting"));
        Element valueBlock = findFirstBlock(valueElement)
                .orElseThrow(() -> new BlocklyParseException("No value block set"));
        String valueType = valueBlock.getAttribute("type");
        BlockParser<ResolvableValue> valueBlockParser = blockParserFactory.getParser(valueType);
        ResolvableValue value = valueBlockParser.parse(valueBlock);

        return new SetState(new ItemValue(itemId, label), value);
    }
}
