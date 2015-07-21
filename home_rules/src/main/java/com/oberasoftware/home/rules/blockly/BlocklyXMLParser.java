package com.oberasoftware.home.rules.blockly;

import com.oberasoftware.home.rules.api.Rule;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Renze de Vries
 */
@Component
public class BlocklyXMLParser implements BlocklyParser {
    private static final Logger LOG = getLogger(BlocklyXMLParser.class);

    private static final DocumentBuilderFactory DOCUMENT_BUILDER_FACTORY = DocumentBuilderFactory.newInstance();

    @Override
    public Rule toRule(String blocklyXml) {
        try {
            DocumentBuilder documentBuilder = DOCUMENT_BUILDER_FACTORY.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            LOG.error("", e);
        }

        return null;
    }
}
