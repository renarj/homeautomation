package com.oberasoftware.home.rules.blockly.general;

import com.oberasoftware.home.api.commands.SwitchCommand;
import com.oberasoftware.home.rules.api.ItemBlock;
import com.oberasoftware.home.rules.api.general.SwitchItem;
import com.oberasoftware.home.rules.blockly.BlockParser;
import com.oberasoftware.home.rules.blockly.BlocklyParseException;
import com.oberasoftware.home.rules.blockly.XMLUtils;
import nl.renarj.core.utilities.StringUtils;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import org.w3c.dom.Element;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Renze de Vries
 */
@Component
public class SwitchItemParser implements BlockParser<ItemBlock> {
    private static final Logger LOG = getLogger(SwitchItemParser.class);

    @Override
    public boolean supportsType(String type) {
        return type.equals("switch_item");
    }

    @Override
    public ItemBlock parse(Element node) throws BlocklyParseException {
        Element fieldElement = XMLUtils.findFieldElement(node, "state")
                .orElseThrow(() -> new BlocklyParseException("Missing state for device switch action"));
        String stateText = fieldElement.getTextContent();

        Element valueElement = XMLUtils.findFirstElement(node, "value")
                .orElseThrow(() -> new BlocklyParseException("No Device specified"));
        Element deviceBlock = XMLUtils.findFirstBlock(valueElement).orElseThrow(() -> new BlocklyParseException("No device specified"));
        String itemDescriptor = deviceBlock.getAttribute("type");


        SwitchCommand.STATE targetState = SwitchCommand.STATE.OFF;
        if(StringUtils.stringNotEmpty(stateText) && stateText.equalsIgnoreCase("on")) {
            targetState = SwitchCommand.STATE.ON;
        }
        LOG.debug("Target state: {} for item: {}", targetState, itemDescriptor);

        return new SwitchItem(getItemId(itemDescriptor), targetState);
    }

    private String getItemId(String itemDescriptor) {
        String itemId = itemDescriptor.substring(itemDescriptor.indexOf(".") + 1);
        LOG.debug("Retrieved itemId: {} from descriptor: {}", itemId, itemDescriptor);

        return itemId;
    }
}
