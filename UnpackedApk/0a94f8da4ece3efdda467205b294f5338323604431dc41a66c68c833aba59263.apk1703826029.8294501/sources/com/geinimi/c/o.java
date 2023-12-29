package com.geinimi.c;

import java.io.InputStream;
import java.util.HashMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public final class o {
    private static DocumentBuilderFactory a = DocumentBuilderFactory.newInstance();
    private static DocumentBuilder b = null;
    private static Document c = null;
    private static Element d = null;
    private static HashMap e = new HashMap();

    public static HashMap a(InputStream inputStream) {
        e.clear();
        DocumentBuilder newDocumentBuilder = a.newDocumentBuilder();
        b = newDocumentBuilder;
        Document parse = newDocumentBuilder.parse(inputStream);
        c = parse;
        Element documentElement = parse.getDocumentElement();
        d = documentElement;
        NodeList elementsByTagName = documentElement.getElementsByTagName("Action");
        if (elementsByTagName.getLength() > 0) {
            for (Node firstChild = elementsByTagName.item(0).getFirstChild(); firstChild != null; firstChild = firstChild.getNextSibling()) {
                if (firstChild.getNodeType() == 1) {
                    String str = "";
                    for (Node firstChild2 = firstChild.getFirstChild(); firstChild2 != null; firstChild2 = firstChild2.getNextSibling()) {
                        str = str + firstChild2.getNodeValue();
                    }
                    "node name:" + firstChild.getNodeName() + "node value:" + str;
                    e.put(firstChild.getNodeName(), str);
                }
            }
        }
        return e;
    }
}
