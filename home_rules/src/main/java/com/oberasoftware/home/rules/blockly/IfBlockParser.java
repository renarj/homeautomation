package com.oberasoftware.home.rules.blockly;

import com.google.common.collect.Lists;
import com.oberasoftware.home.rules.api.Action;
import com.oberasoftware.home.rules.api.Condition;
import com.oberasoftware.home.rules.api.IfBlock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Element;

import static com.oberasoftware.home.rules.blockly.XMLUtils.findFirstBlock;
import static com.oberasoftware.home.rules.blockly.XMLUtils.findFirstElement;

/**
 * @author Renze de Vries
 */
@Component
public class IfBlockParser implements FragmentParser<IfBlock> {

    @Autowired
    private FragmentParserFactory fragmentParserFactory;

    @Override
    public boolean supportsType(String type) {
        return type.equals("controls_if");
    }

    @Override
    public IfBlock parse(Element node) throws BlocklyParseException {
        Element ifCondition = findFirstElement(node, "value").orElseThrow(() ->
                new BlocklyParseException("No IF condition specified"));
        Element conditionBlock = findFirstBlock(ifCondition)
                .orElseThrow(() -> new BlocklyParseException("No Condition specified"));
        FragmentParser<Condition> conditionFragmentParser = fragmentParserFactory.getParser(conditionBlock.getAttribute("type"));
        Condition condition = conditionFragmentParser.parse(conditionBlock);


        Element doStatement = findFirstElement(node, "statement").orElseThrow(() ->
                new BlocklyParseException("No DO statement specified"));
        Element statementBlock = findFirstBlock(doStatement)
                .orElseThrow(() -> new BlocklyParseException("No statement block specified"));

        FragmentParser<Action> actionFragmentParser =
                fragmentParserFactory.getParser(statementBlock.getAttribute("type"));
        Action action = actionFragmentParser.parse(statementBlock);

        return new IfBlock(condition, Lists.newArrayList(action));
    }
}
