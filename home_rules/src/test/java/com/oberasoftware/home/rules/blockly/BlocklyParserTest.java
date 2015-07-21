package com.oberasoftware.home.rules.blockly;

import com.google.common.io.CharSource;
import com.google.common.io.Resources;
import com.oberasoftware.home.rules.api.Rule;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.Charset;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * @author Renze de Vries
 */
public class BlocklyParserTest {
    @Test
    public void testParseSimpleRule() throws IOException {
        CharSource s = Resources.asCharSource(this.getClass().getResource("/simple_luminance_rule.xml"), Charset.defaultCharset());
        String blocklyRuleXML = s.read();

        Rule rule = new BlocklyXMLParser().toRule(blocklyRuleXML);
        assertThat(rule, notNullValue());


    }
}
