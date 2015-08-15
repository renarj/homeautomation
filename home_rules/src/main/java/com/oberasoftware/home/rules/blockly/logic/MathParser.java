package com.oberasoftware.home.rules.blockly.logic;

import com.oberasoftware.home.rules.api.MathOperator;
import com.oberasoftware.home.rules.api.values.MathValue;
import com.oberasoftware.home.rules.api.values.ResolvableValue;
import com.oberasoftware.home.rules.blockly.BaseValueParser;
import com.oberasoftware.home.rules.blockly.BlockParser;
import com.oberasoftware.home.rules.blockly.BlocklyParseException;
import com.oberasoftware.home.rules.blockly.XMLUtils;
import org.springframework.stereotype.Component;
import org.w3c.dom.Element;

/**
 * @author Renze de Vries
 */
@Component
public class MathParser extends BaseValueParser implements BlockParser<MathValue> {
    @Override
    public boolean supportsType(String type) {
        return "math_arithmetic".equalsIgnoreCase(type);
    }

    @Override
    public MathValue parse(Element node) throws BlocklyParseException {
        MathOperator operator = getOperator(node);
        ResolvableValue leftValue = getResolvableValue(node, "A");
        ResolvableValue rightValue = getResolvableValue(node, "B");

        return new MathValue(leftValue, rightValue, operator);
    }

    private MathOperator getOperator(Element node) throws BlocklyParseException {
        Element operatorElement = XMLUtils.findFieldElement(node, "OP")
                .orElseThrow(() -> new BlocklyParseException("No operator specified"));
        if(operatorElement.getTextContent().equalsIgnoreCase("MINUS")) {
            return MathOperator.MINUS;
        } else {
            return MathOperator.PLUS;
        }
    }
}
