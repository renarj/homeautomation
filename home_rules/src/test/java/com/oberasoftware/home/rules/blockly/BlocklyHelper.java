package com.oberasoftware.home.rules.blockly;

import com.google.common.io.CharSource;
import com.google.common.io.Resources;
import com.oberasoftware.home.rules.api.general.Rule;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * @author Renze de Vries
 */
public class BlocklyHelper {
    public static Rule parseRule(BlocklyParser parser, String ruleFile) throws BlocklyParseException, IOException {
        CharSource s = Resources.asCharSource(BlocklyHelper.class.getResource(ruleFile), Charset.defaultCharset());
        String blocklyRuleXML = s.read();

        return parser.toRule(blocklyRuleXML);
    }

}
