package com.pananfly.oauth2.platform;

import com.pananfly.oauth2.exception.OAuth2Exception;

/**
 * Platform action listener
 * @author pananfly
 */
public interface PlatformActionListener {
    void onError(Platform platform, OAuth2Exception exception);
    void onComplete(Platform platform);
    void onCancel(Platform platform);
}
