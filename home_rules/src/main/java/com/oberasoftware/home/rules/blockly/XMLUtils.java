package com.oberasoftware.home.rules.blockly;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.oberasoftware.home.rules.blockly.XMLConstants.*;
import static com.oberasoftware.home.rules.blockly.XMLConstants.BLOCK;
import static com.oberasoftware.home.rules.blockly.XMLConstants.ROOT;

/**
 * @author Renze de Vries
 */
public class XMLUtils {

    private XMLUtils() {

    }

    public static List<Element> asList(NodeList nodes) {
        List<Element> elements = new ArrayList<>();
        for(int i=0; i<nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if(node instanceof Element) {
                elements.add((Element) node);
            }
        }

        return elements;
    }

    public static Map<String, List<Element>> asMap(NodeList nodes) {
        List<Element> elements = asList(nodes);

        return elements.stream().collect(Collectors.groupingBy(Element::getTagName));
    }

    public static Optional<Element> findFieldElement(Element parent, String fieldName) {
        return findElementWithAttribute(parent, FIELD_STATEMENT, NAME_ATTRIBUTE, fieldName);
    }

    public static Optional<Element> findBlockOfType(Element parent, String type) {
        return findElementWithAttribute(parent, BLOCK, BLOCK_TYPE, type);
    }

    public static Optional<Element> findFirstElement(Element parent, String elementName) {
        Map<String, List<Element>> childMap = asMap(parent.getChildNodes());
        if(childMap.containsKey(elementName)) {
            List<Element> blocks = childMap.get(elementName);

            return blocks.stream().findFirst();
        }

        return Optional.empty();

    }

    public static Optional<Element> findFirstBlock(Element parent) {
        return findFirstElement(parent, BLOCK);
    }

    public static Optional<Element> findRootXmlNode(Document document) {
        return asList(document.getChildNodes()).stream().filter(e -> e.getTagName().equals(ROOT)).findFirst();
    }

    public static Optional<Element> findElementWithAttribute(Element parent, String elementName, String attributeName, String value) {
        Map<String, List<Element>> childMap = asMap(parent.getChildNodes());
        if(childMap.containsKey(elementName)) {
            List<Element> elements = childMap.get(elementName);
            return elements.stream().filter(e -> e.getAttribute(attributeName).equals(value)).findFirst();
        }
        return Optional.empty();
    }
}
