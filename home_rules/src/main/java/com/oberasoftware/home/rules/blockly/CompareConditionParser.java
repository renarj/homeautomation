package com.oberasoftware.home.rules.blockly;

import com.oberasoftware.home.rules.api.CompareCondition;
import com.oberasoftware.home.rules.api.Operator;
import com.oberasoftware.home.rules.api.ResolvableValue;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Element;

import static com.oberasoftware.home.rules.api.Operator.EQUALS;
import static com.oberasoftware.home.rules.api.Operator.LARGER_THAN;
import static com.oberasoftware.home.rules.api.Operator.LARGER_THAN_EQUALS;
import static com.oberasoftware.home.rules.api.Operator.SMALLER_THAN;
import static com.oberasoftware.home.rules.api.Operator.SMALLER_THAN_EQUALS;
import static com.oberasoftware.home.rules.blockly.XMLUtils.findElementWithAttribute;
import static com.oberasoftware.home.rules.blockly.XMLUtils.findFieldElement;
import static com.oberasoftware.home.rules.blockly.XMLUtils.findFirstBlock;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Renze de Vries
 */
@Component
public class CompareConditionParser implements FragmentParser<CompareCondition> {
    private static final Logger LOG = getLogger(CompareConditionParser.class);

    @Autowired
    private FragmentParserFactory fragmentParserFactory;

    @Override
    public boolean supportsType(String type) {
        return type.equals("logic_compare");
    }

    @Override
    public CompareCondition parse(Element node) throws BlocklyParseException {
        String operatorText = findFieldElement(node, "OP")
                .orElseThrow(() -> new BlocklyParseException("No operator defined")).getTextContent();
        Operator operator = getOperator(operatorText);
        LOG.debug("Operator: {}", operator);

        ResolvableValue leftValue = getResolvableValue(node, "A");
        ResolvableValue rightValue = getResolvableValue(node, "B");

        return new CompareCondition(leftValue, operator, rightValue);
    }

    private ResolvableValue getResolvableValue(Element node, String paramName) throws BlocklyParseException {
        Element value = findElementWithAttribute(node, "value", "name", paramName)
                .orElseThrow(() -> new BlocklyParseException("No value specified in compare condition"));
        Element valueBlock = findFirstBlock(value)
                .orElseThrow(() -> new BlocklyParseException("No condition specified in side of compare"));

        String blockType = valueBlock.getAttribute("type");
        FragmentParser<ResolvableValue> valueFragmentParser = fragmentParserFactory.getParser(blockType);
        return valueFragmentParser.parse(valueBlock);
    }

    private Operator getOperator(String operator) {
        switch(operator) {
            case "LTE":
                return SMALLER_THAN_EQUALS;
            case "LT":
                return SMALLER_THAN;
            case "GTE":
                return LARGER_THAN_EQUALS;
            case "GT":
                return LARGER_THAN;
            case "EQ":
            default:
                return EQUALS;
        }
    }
}
