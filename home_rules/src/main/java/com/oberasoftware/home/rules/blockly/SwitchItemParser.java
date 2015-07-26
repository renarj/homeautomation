package com.oberasoftware.home.rules.blockly;

import com.oberasoftware.home.api.commands.SwitchCommand;
import com.oberasoftware.home.rules.api.ItemAction;
import com.oberasoftware.home.rules.api.SwitchAction;
import nl.renarj.core.utilities.StringUtils;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import org.w3c.dom.Element;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Renze de Vries
 */
@Component
public class SwitchItemParser implements FragmentParser<ItemAction> {
    private static final Logger LOG = getLogger(SwitchItemParser.class);

    @Override
    public boolean supportsType(String type) {
        return type.equals("switch_item");
    }

    @Override
    public ItemAction parse(Element node) throws BlocklyParseException {
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

        return new SwitchAction(getItemId(itemDescriptor), targetState);
    }

    private String getItemId(String itemDescriptor) {
        String itemId = itemDescriptor.substring(itemDescriptor.indexOf(".") + 1);
        LOG.debug("Retrieved itemId: {} from descriptor: {}", itemId, itemDescriptor);

        return itemId;
    }
}
