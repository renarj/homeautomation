package com.oberasoftware.home.rules.blockly;

import com.oberasoftware.home.rules.api.general.Rule;

/**
 * @author Renze de Vries
 */
public interface BlocklyParser {
    Rule toRule(String blocklyXml) throws BlocklyParseException;
}
