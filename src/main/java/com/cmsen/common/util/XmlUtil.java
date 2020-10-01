/**
 * +---------------------------------------------------------
 * | Author Jared.Yan<yanhuaiwen@163.com>
 * +---------------------------------------------------------
 * | Copyright (c) http://cmsen.com All rights reserved.
 * +---------------------------------------------------------
 */
package com.cmsen.common.util;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.LinkedHashMap;
import java.util.Map;

public class XmlUtil {
    public static Map<String, Object> toObject(String xml) throws Exception {
        return parse(reader(xml).getDocumentElement().getChildNodes());
    }

    public static String toXml(Map<String, Object> o) throws Exception {
        return create(o, false);
    }

    public static String toXml(Map<String, Object> o, boolean header) throws Exception {
        return create(o, header);
    }

    public static <T> T toClass(String xml, Class<T> clazz) throws Exception {
        return JsonUtil.toClass(JsonUtil.toString(toObject(xml)), clazz);
    }

    public static Map<String, Object> parse(NodeList nodeList) {
        Map<String, Object> data = new LinkedHashMap<>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                NodeList childNode = element.getChildNodes();
                if (childNode.getLength() > Node.ELEMENT_NODE) {
                    data.put(element.getNodeName(), parse(childNode));
                } else {
                    data.put(element.getNodeName(), element.getTextContent());
                }
            }
        }
        return data;
    }

    private static Document reader(String xml) throws Exception {
        StringReader stringReader = new StringReader(xml);
        InputSource inputSource = new InputSource(stringReader);
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        return documentBuilder.parse(inputSource);
    }

    private static String create(Map<String, Object> data, boolean header) throws Exception {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.newDocument();
        document.setXmlStandalone(true);
        Element root = document.createElement("xml");
        document.appendChild(root);
        for (String key : data.keySet()) {
            Object value = data.get(key);
            Element filed = document.createElement(key);
            if (value instanceof Map) {
                Map<?, ?> value1 = (Map<?, ?>) value;
                for (Object o : value1.keySet()) {
                    Object val = value1.get(o);
                    Element filed1 = document.createElement(String.valueOf(o));
                    if (val instanceof String) {
                        filed1.appendChild(document.createCDATASection(String.valueOf(val)));
                    } else {
                        filed1.appendChild(document.createTextNode(String.valueOf(val)));
                    }
                    filed.appendChild(filed1);
                }
                root.appendChild(filed);

            } else {
                if (value instanceof String) {
                    filed.appendChild(document.createCDATASection(String.valueOf(value)));
                } else {
                    filed.appendChild(document.createTextNode(String.valueOf(value)));
                }
                root.appendChild(filed);
            }
        }
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        DOMSource source = new DOMSource(document);
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        transformer.transform(source, result);
        String output = writer.getBuffer().toString();
        if (!header) {
            output = output.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "");
        }
        try {
            writer.close();
        } catch (Exception ex) {
        }
        return output;
    }
}
