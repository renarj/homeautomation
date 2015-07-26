package com.oberasoftware.home.rules.blockly;

import com.oberasoftware.home.api.exceptions.HomeAutomationException;

/**
 * @author Renze de Vries
 */
public class BlocklyParseException extends HomeAutomationException {
    public BlocklyParseException(String message, Throwable e) {
        super(message, e);
    }

    public BlocklyParseException(String message) {
        super(message);
    }
}
