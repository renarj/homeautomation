package com.oberasoftware.home.rules.blockly.values;

import com.google.common.primitives.Doubles;
import com.google.common.primitives.Longs;
import com.oberasoftware.home.api.types.VALUE_TYPE;
import com.oberasoftware.home.rules.api.values.ResolvableValue;
import com.oberasoftware.home.rules.api.values.StaticValue;
import com.oberasoftware.home.rules.blockly.BlockParser;
import com.oberasoftware.home.rules.blockly.BlocklyParseException;
import org.springframework.stereotype.Component;
import org.w3c.dom.Element;

import static com.oberasoftware.home.rules.blockly.XMLUtils.findFieldElement;

/**
 * @author Renze de Vries
 */
@Component
public class StaticValueParser implements BlockParser<ResolvableValue> {
    @Override
    public boolean supportsType(String type) {
        return "math_number".equals(type);
    }

    @Override
    public ResolvableValue parse(Element node) throws BlocklyParseException {
        Element numberField = findFieldElement(node, "NUM")
                .orElseThrow(() -> new BlocklyParseException("No Value field specified"));
        numberField.getTextContent();

        return new StaticValue(getNumber(numberField.getTextContent()), VALUE_TYPE.NUMBER);
    }

    private Object getNumber(String text) {
        Long result = Longs.tryParse(text);
        if(result == null) {
            return Doubles.tryParse(text);
        } else {
            return result;
        }
    }
}
