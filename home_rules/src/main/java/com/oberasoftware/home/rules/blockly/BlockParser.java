package com.oberasoftware.home.rules.blockly;

import org.w3c.dom.Element;

/**
 * @author Renze de Vries
 */
public interface BlockParser<T> {
    boolean supportsType(String type);

    T parse(Element node) throws BlocklyParseException;
}
