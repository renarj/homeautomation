package com.oberasoftware.home.rules.blockly;

import com.oberasoftware.home.rules.api.general.Rule;
import com.oberasoftware.home.rules.blockly.general.RuleBlockParser;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;

import static com.oberasoftware.home.rules.blockly.XMLUtils.findRootXmlNode;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Renze de Vries
 */
@Component
public class BlocklyXMLParser implements BlocklyParser {
    private static final Logger LOG = getLogger(BlocklyXMLParser.class);

    private static final DocumentBuilderFactory DOCUMENT_BUILDER_FACTORY = DocumentBuilderFactory.newInstance();

    @Autowired
    private RuleBlockParser ruleBlockParser;

    @Override
    public Rule toRule(String blocklyXml) throws BlocklyParseException {
        try {
            DocumentBuilder documentBuilder = DOCUMENT_BUILDER_FACTORY.newDocumentBuilder();
            Document document = documentBuilder.parse(new InputSource(new StringReader(blocklyXml)));
            Element rootElement = findRootXmlNode(document).orElseThrow(() -> new BlocklyParseException("Could not find Blockly root xml node"));

            return ruleBlockParser.parse(XMLUtils.findBlockOfType(rootElement, "rule")
                    .orElseThrow(() -> new BlocklyParseException("Could not find Rule block")));
        } catch (ParserConfigurationException | SAXException | IOException e) {
            LOG.error("", e);
            throw new BlocklyParseException("Unable to parse blockly xml", e);
        }
    }
}
