package com.oberasoftware.home.rules.blockly;

import com.oberasoftware.home.rules.api.Block;
import com.oberasoftware.home.rules.api.Rule;
import com.oberasoftware.home.rules.api.Trigger;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Element;

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
public class RuleFragmentParser implements FragmentParser<Rule> {
    private static final Logger LOG = getLogger(RuleFragmentParser.class);

    @Autowired
    private TriggerParser triggerParser;

    @Autowired
    private FragmentParserFactory fragmentParserFactory;

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

            Trigger trigger = triggerParser.parse(node);
            LOG.debug("We have a trigger: {}", trigger);

            Element statementElement = findElementWithAttribute(node, "statement", NAME_ATTRIBUTE, "ruleStatement")
                    .orElseThrow(() -> new BlocklyParseException("Missing rule statement"));


            Element blockElement = findFirstBlock(statementElement).orElseThrow(() -> new BlocklyParseException("No statements found for rule"));
            Block block = getBlock(blockElement);

            return new Rule(ruleName, block, trigger);
        }


        return null;

    }

    private Block getBlock(Element blockElement) throws BlocklyParseException{
        String blockType = blockElement.getAttribute("type");

        FragmentParser<Block> blockFragmentParser = fragmentParserFactory.getParser(blockType);
        return blockFragmentParser.parse(blockElement);
    }
}
