package com.oberasoftware.home.rules.blockly;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Renze de Vries
 */
@Component
public class BlockParserFactory {
    @Autowired(required = false)
    private List<BlockParser<?>> blockParsers;

    public <T> BlockParser<T> getParser(String type) throws BlocklyParseException {
        BlockParser<?> parser = blockParsers.stream().filter(p -> p.supportsType(type)).findFirst()
                .orElseThrow(() -> new BlocklyParseException("Unable to find parser for block type: " + type));

        return (BlockParser<T>) parser;
    }
}
