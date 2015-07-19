package com.oberasoftware.home.rules.blockly;

import com.oberasoftware.home.rules.api.Rule;
import org.springframework.stereotype.Component;

/**
 * @author Renze de Vries
 */
@Component
public class BlocklyXMLParser implements BlocklyParser {
    @Override
    public Rule toRule(String blocklyXml) {
        return null;
    }
}
