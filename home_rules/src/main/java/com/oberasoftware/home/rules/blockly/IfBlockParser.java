package com.oberasoftware.home.rules.blockly;

import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;
import com.oberasoftware.home.rules.api.Action;
import com.oberasoftware.home.rules.api.Condition;
import com.oberasoftware.home.rules.api.IfBlock;
import com.oberasoftware.home.rules.api.IfBranch;
import nl.renarj.core.utilities.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.oberasoftware.home.rules.blockly.XMLUtils.findElementWithAttribute;
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
        Optional<Element> mutationElement = XMLUtils.findFirstElement(node, "mutation");
        boolean elsePresent = false;
        int elseIfsPresent = 0;
        if(mutationElement.isPresent()) {
            Optional<Integer> elseAttribute = getValue(mutationElement.get(), "else");
            Optional<Integer> elseIfAttribute = getValue(mutationElement.get(), "elseif");

            elsePresent = elseAttribute.isPresent();
            elseIfsPresent = elseIfAttribute.isPresent() ? elseIfAttribute.get() : 0;
        }

        List<IfBranch> branches = new ArrayList<>();
        for(int i=0; i<=elseIfsPresent; i++) {
            String ifName = "IF" + i;
            String statementName = "DO" + i;

            branches.add(getBranch(node, ifName, statementName));
        }

        if(elsePresent) {
            branches.add(getBranch(node, "NOT", "ELSE"));
        }

//        Element ifCondition = findFirstElement(node, "value").orElseThrow(() ->
//                new BlocklyParseException("No IF condition specified"));
//        Element conditionBlock = findFirstBlock(ifCondition)
//                .orElseThrow(() -> new BlocklyParseException("No Condition specified"));
//        FragmentParser<Condition> conditionFragmentParser = fragmentParserFactory.getParser(conditionBlock.getAttribute("type"));
//        Condition condition = conditionFragmentParser.parse(conditionBlock);
//
//
//        Element doStatement = findFirstElement(node, "statement").orElseThrow(() ->
//                new BlocklyParseException("No DO statement specified"));
//        Element statementBlock = findFirstBlock(doStatement)
//                .orElseThrow(() -> new BlocklyParseException("No statement block specified"));
//
//        FragmentParser<Action> actionFragmentParser =
//                fragmentParserFactory.getParser(statementBlock.getAttribute("type"));
//        Action action = actionFragmentParser.parse(statementBlock);
//
//        return new IfBlock(condition, Lists.newArrayList(action));

        return new IfBlock(branches);
    }

    private IfBranch getBranch(Element node, String ifBranchName, String statementName) throws BlocklyParseException {
        Element doStatement = findFirstElement(node, "statement").orElseThrow(() ->
                new BlocklyParseException("No DO statement specified"));
        Element statementBlock = findFirstBlock(doStatement)
                .orElseThrow(() -> new BlocklyParseException("No statement block specified"));

        FragmentParser<Action> actionFragmentParser =
                fragmentParserFactory.getParser(statementBlock.getAttribute("type"));
        Action action = actionFragmentParser.parse(statementBlock);
        List<Action> actions = Lists.newArrayList(action);

        //in case of an ELSE operation, condition is not present
        Optional<Element> ifCondition = findElementWithAttribute(node, "value", "name", ifBranchName);
        if(ifCondition.isPresent()) {
            Element conditionBlock = findFirstBlock(ifCondition.get())
                    .orElseThrow(() -> new BlocklyParseException("No Condition specified"));
            FragmentParser<Condition> conditionFragmentParser = fragmentParserFactory.getParser(conditionBlock.getAttribute("type"));
            Condition condition = conditionFragmentParser.parse(conditionBlock);

            return new IfBranch(condition, actions);
        }

        return new IfBranch(null, actions);
    }

    private Optional<Integer> getValue(Element node, String attribute) {
        String value = node.getAttribute(attribute);
        if(StringUtils.stringNotEmpty(value)) {
            return Optional.ofNullable(Ints.tryParse(value));
        }

        return Optional.empty();
    }
}
