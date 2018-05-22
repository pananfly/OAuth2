package com.pananfly.oauth2;

import com.pananfly.oauth2.exception.OAuth2Exception;

/**
 * OAuth2 ui listener
 * @author pananfly
 */
public interface OAuth2Listener {
    void onCancle();
    void onError(OAuth2Exception e);
    void onComplete(String result);
}
