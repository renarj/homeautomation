package com.oberasoftware.home.rules.blockly.logic;

import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;
import com.oberasoftware.home.rules.api.Block;
import com.oberasoftware.home.rules.api.Condition;
import com.oberasoftware.home.rules.api.logic.IfBlock;
import com.oberasoftware.home.rules.api.logic.IfBranch;
import com.oberasoftware.home.rules.blockly.BlockParser;
import com.oberasoftware.home.rules.blockly.BlockParserFactory;
import com.oberasoftware.home.rules.blockly.BlocklyParseException;
import com.oberasoftware.home.rules.blockly.XMLUtils;
import nl.renarj.core.utilities.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.oberasoftware.home.rules.blockly.XMLUtils.findElementWithAttribute;
import static com.oberasoftware.home.rules.blockly.XMLUtils.findFirstBlock;

/**
 * @author Renze de Vries
 */
@Component
public class IfBlockParser implements BlockParser<IfBlock> {

    @Autowired
    private BlockParserFactory blockParserFactory;

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
        return new IfBlock(branches);
    }

    private IfBranch getBranch(Element node, String ifBranchName, String statementName) throws BlocklyParseException {
        Element doStatement = findElementWithAttribute(node, "statement", "name", statementName).orElseThrow(() ->
                new BlocklyParseException("No DO statement specified"));
        Element statementBlock = findFirstBlock(doStatement)
                .orElseThrow(() -> new BlocklyParseException("No statement block specified"));
        List<Block> statements = getStatements(statementBlock);

        //in case of an ELSE operation, condition is not present
        Optional<Element> ifCondition = findElementWithAttribute(node, "value", "name", ifBranchName);
        if(ifCondition.isPresent()) {
            Element conditionBlock = findFirstBlock(ifCondition.get())
                    .orElseThrow(() -> new BlocklyParseException("No Condition specified"));
            BlockParser<Condition> conditionBlockParser = blockParserFactory.getParser(conditionBlock.getAttribute("type"));
            Condition condition = conditionBlockParser.parse(conditionBlock);

            return new IfBranch(condition, statements);
        }

        return new IfBranch(null, statements);
    }

    private List<Block> getStatements(Element statementBlock) throws BlocklyParseException {
        BlockParser<Block> actionBlockParser =
                blockParserFactory.getParser(statementBlock.getAttribute("type"));
        Block statement = actionBlockParser.parse(statementBlock);
        List<Block> statements = Lists.newArrayList(statement);

        Optional<Element> optionalNext = XMLUtils.findFirstElement(statementBlock, "next");
        if(optionalNext.isPresent()) {
            Element nextBlock = XMLUtils.findFirstBlock(optionalNext.get()).get();
            statements.addAll(getStatements(nextBlock));
        }

        return statements;
    }

    private Optional<Integer> getValue(Element node, String attribute) {
        String value = node.getAttribute(attribute);
        if(StringUtils.stringNotEmpty(value)) {
            return Optional.ofNullable(Ints.tryParse(value));
        }

        return Optional.empty();
    }
}
