package com.pananfly.oauth2;

import android.util.SparseArray;

import com.pananfly.oauth2.platform.PlatformConfigure;
import com.pananfly.oauth2.xml.IXmlCallback;
import com.pananfly.oauth2.xml.XmlParseRunnable;

import org.xml.sax.Attributes;

import java.io.InputStream;

/**
 * OAuth2Configures read from xml
 * @author pananfly
 */
public class OAuth2Configures {

    private static SparseArray<PlatformConfigure> mPlatformConfigs = new SparseArray<>();
    public static void readConfigures(InputStream ins){
        new Thread(new XmlParseRunnable(ins, new IXmlCallback() {
            @Override
            public void readFailed(String errMsg) {

            }

            @Override
            public void startElement(String uri, String localName, String qName, Attributes attributes) {
                if(qName.equals("Platform")){
                    return;
                }
                try {
                    int id = Integer.parseInt(attributes.getValue("Id"));
                    String appKey = attributes.getValue("AppKey");
                    String appSecret = attributes.getValue("AppSecret");
                    String redirectUrl = attributes.getValue("RedirectUrl");
                    PlatformConfigure configure = new PlatformConfigure(id , qName ,  appKey , appSecret , redirectUrl);
                    mPlatformConfigs.put(id , configure);
                }catch (Exception e){

                }
            }

            @Override
            public void endElement(String uri, String localName, String qName) {

            }

            @Override
            public void characters(char[] ch, int start, int length) {

            }
        })).start();
    }

    public static PlatformConfigure getConfigure(int id){
        return mPlatformConfigs.get(id);
    }
}
