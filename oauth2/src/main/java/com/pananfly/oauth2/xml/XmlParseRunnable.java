package com.pananfly.oauth2.xml;

import android.support.annotation.NonNull;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Xml parse runnable
 * @author pananfly
 */
public class XmlParseRunnable extends DefaultHandler implements Runnable {
    private InputStream mInputStream;
    private IXmlCallback mCallback;
    public XmlParseRunnable(@NonNull InputStream ins , IXmlCallback callback){
        mCallback = callback;
        mInputStream = ins;
    }

    @Override
    public void run() {
        boolean failed = false;
        String errMsg = null;
        try{
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser parser = spf.newSAXParser();
            parser.parse(mInputStream , this);
        }catch (SAXException e){
            e.printStackTrace();
            failed = true;
            errMsg = e.getMessage();
        }catch (ParserConfigurationException e){
            e.printStackTrace();
            failed = true;
            errMsg = e.getMessage();
        }catch (IOException e){
            e.printStackTrace();
            failed = true;
            errMsg = e.getMessage();
        }
        if(failed && mCallback != null){
            mCallback.readFailed(errMsg);
        }
        if(mInputStream != null){
            try {
                mInputStream.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        if(mCallback != null) {
            mCallback.startElement(uri, localName, qName, attributes);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        if(mCallback != null) {
            mCallback.endElement(uri, localName, qName);
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        if(mCallback != null) {
            mCallback.characters(ch, start, length);
        }
    }
}
