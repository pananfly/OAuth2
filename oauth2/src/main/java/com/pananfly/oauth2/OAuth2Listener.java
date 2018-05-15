package com.pananfly.oauth2;

/**
 * OAuth2 ui listener
 * @author pananfly
 */
public interface OAuth2Listener {
    void onCancle();
    void onComplete(String result);
}
