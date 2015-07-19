package com.oberasoftware.home.rules.blockly;

import com.oberasoftware.home.rules.api.Rule;

/**
 * @author Renze de Vries
 */
public interface BlocklyParser {
    Rule toRule(String blocklyXml);
}
