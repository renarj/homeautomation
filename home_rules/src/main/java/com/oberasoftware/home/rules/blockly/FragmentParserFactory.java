package com.oberasoftware.home.rules.blockly;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Renze de Vries
 */
@Component
public class FragmentParserFactory {

    @Autowired(required = false)
    private List<FragmentParser<?>> fragmentParsers;

    public <T> FragmentParser<T> getParser(String type) throws BlocklyParseException {
        FragmentParser<?> parser = fragmentParsers.stream().filter(p -> p.supportsType(type)).findFirst()
                .orElseThrow(() -> new BlocklyParseException("Unable to find parser for block type: " + type));

        return (FragmentParser<T>) parser;
    }
}
