package com.pananfly.oauth2.exception;

/**
 * OAuth2 exception
 * @author pananfly
 */
public class OAuth2Exception extends Throwable {
    public OAuth2Exception(String message) {
        super(message);
    }

    public OAuth2Exception(String message, Throwable cause) {
        super(message, cause);
    }
}
