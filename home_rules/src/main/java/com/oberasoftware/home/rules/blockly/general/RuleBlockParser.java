package com.oberasoftware.home.rules.blockly.general;

import com.oberasoftware.home.rules.api.Block;
import com.oberasoftware.home.rules.api.general.Rule;
import com.oberasoftware.home.rules.api.trigger.Trigger;
import com.oberasoftware.home.rules.blockly.BlockParser;
import com.oberasoftware.home.rules.blockly.BlockParserFactory;
import com.oberasoftware.home.rules.blockly.BlocklyParseException;
import com.oberasoftware.home.rules.blockly.XMLUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.oberasoftware.home.rules.blockly.XMLConstants.NAME_ATTRIBUTE;
import static com.oberasoftware.home.rules.blockly.XMLConstants.RULE_NAME;
import static com.oberasoftware.home.rules.blockly.XMLUtils.findElementWithAttribute;
import static com.oberasoftware.home.rules.blockly.XMLUtils.findFieldElement;
import static com.oberasoftware.home.rules.blockly.XMLUtils.findFirstBlock;
import static org.slf4j.LoggerFactory.getLogger;


/**
 * @author Renze de Vries
 */
@Component
public class RuleBlockParser implements BlockParser<Rule> {
    private static final Logger LOG = getLogger(RuleBlockParser.class);

    @Autowired
    private BlockParserFactory blockParserFactory;

    @Override
    public boolean supportsType(String type) {
        return type.equals("rule");
    }

    @Override
    public Rule parse(Element node) throws BlocklyParseException {
        Optional<Element> ruleNameElement = findFieldElement(node, RULE_NAME);
        if(ruleNameElement.isPresent()) {
            String ruleName = ruleNameElement.get().getTextContent();

            LOG.debug("Determined rule name: {}", ruleName);

            Element triggerElement = findElementWithAttribute(node, "statement", NAME_ATTRIBUTE, "ruleTrigger")
                    .orElseThrow(() -> new BlocklyParseException("No trigger was defined for rule"));
            List<Trigger> triggers = getTriggers(triggerElement);

            LOG.debug("We have found: {} triggers: {}", triggers.size(), triggers);

            Element statementElement = findElementWithAttribute(node, "statement", NAME_ATTRIBUTE, "ruleStatement")
                    .orElseThrow(() -> new BlocklyParseException("Missing rule statement"));


            Element blockElement = findFirstBlock(statementElement).orElseThrow(() -> new BlocklyParseException("No statements found for rule"));
            Block block = getBlock(blockElement);

            return new Rule(null, ruleName, block, triggers);
        }

        return null;

    }

    private List<Trigger> getTriggers(Element triggerElement) throws BlocklyParseException {
        List<Trigger> triggers = new ArrayList<>();

        Element triggerBlock = findFirstBlock(triggerElement)
                .orElseThrow(() -> new BlocklyParseException("No trigger block specified"));
        String triggerType = triggerBlock.getAttribute("type");

        BlockParser<Trigger> triggerBlockParser = blockParserFactory.getParser(triggerType);
        Trigger trigger = triggerBlockParser.parse(triggerBlock);
        triggers.add(trigger);

        Optional<Element> nextTrigger = XMLUtils.findFirstElement(triggerBlock, "next");
        if(nextTrigger.isPresent()) {
            triggers.addAll(getTriggers(nextTrigger.get()));
        }
        return triggers;
    }

    private Block getBlock(Element blockElement) throws BlocklyParseException{
        String blockType = blockElement.getAttribute("type");

        BlockParser<Block> blockBlockParser = blockParserFactory.getParser(blockType);
        return blockBlockParser.parse(blockElement);
    }
}
