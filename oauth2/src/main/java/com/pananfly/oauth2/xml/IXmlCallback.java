package com.pananfly.oauth2.xml;

import org.xml.sax.Attributes;

/**
 * Xml parse callback
 * @author pananfly
 */
public interface IXmlCallback {
    void readFailed(String errMsg);
    void startElement(String uri, String localName, String qName, Attributes attributes);
    void endElement(String uri, String localName, String qName);
    void characters(char[] ch, int start, int length);
}
