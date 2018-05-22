package com.pananfly.oauth2.platform;

import android.app.PendingIntent;
import android.content.Intent;
import android.text.TextUtils;

import com.pananfly.oauth2.OAuth2;
import com.pananfly.oauth2.OAuth2Configures;
import com.pananfly.oauth2.OAuth2Listener;
import com.pananfly.oauth2.OAuth2SharePreference;
import com.pananfly.oauth2.OAuth2UI;
import com.pananfly.oauth2.exception.OAuth2Exception;

/**
 * Platform abstract class
 * @author pananfly
 */
public abstract class Platform{

    private PlatformConfigure mConfigure;
    private OAuth2SharePreference mSp;

    public Platform(){
        mConfigure = OAuth2Configures.getConfigure(getPlatformId());
        mSp = new OAuth2SharePreference(OAuth2.getContext());
        OAuth2.registerOAuth2Listener(mOAuth2UIListener);
    }

    /* platform callback listener */
    private PlatformActionListener mListener;

    /* Platform with ui listener */
    private OAuth2Listener mOAuth2UIListener = new OAuth2Listener() {
        @Override
        public void onCancle() {
            if(getActionListener() != null){
                getActionListener().onCancel(Platform.this);
            }
        }

        @Override
        public void onError(OAuth2Exception e) {
            if(getActionListener() != null){
                getActionListener().onError(Platform.this , e);
            }
        }

        @Override
        public void onComplete(String result) {
            authorizeResult(result);
        }
    };

    /**
     * Ger platform name
     * @return
     */
    public abstract String getPlatformName();

    /**
     *
     * @return
     */
    public abstract int getPlatformId();

    /**
     * Webview's page url result
     * @param result
     */
    public abstract void authorizeResult(String result);

    /**
     * Get oauth access url
     * @param appKey
     * @param appSecret
     * @param redirectUrl
     * @return
     */
    public abstract String getUrl(String appKey , String appSecret , String redirectUrl);

    /**
     * Get OAuth2 SharePreference
     * @return
     */
    public OAuth2SharePreference getSp(){
        return mSp;
    }

    /**
     * Get your platform's appkey
     * @return
     */
    public String getAppKey(){
        if(mConfigure == null){
            return null;
        }
        return mConfigure.getAppKey();
    }

    /**
     * Get your platform's appSecret
     * @return
     */
    public String getAppSecret(){
        if(mConfigure == null){
            return null;
        }
        return mConfigure.getAppSecret();
    }

    /**
     * Get your platform's redirectUrl
     * @return
     */
    public String getRedirectUrl(){
        if(mConfigure == null){
            return null;
        }
        return mConfigure.getRedirectUrl();
    }

    /**
     * Set action listener
     * @param listener
     */
    public void setActionListener(PlatformActionListener listener){
        mListener = listener;
    }

    /**
     * Get Platform action listener
     * @return
     */
    protected PlatformActionListener getActionListener(){
        return mListener;
    }

    /**
     * Start authorize
     */
    public void autorize(){
        //check configure
        if(mConfigure == null){
            if(getActionListener() != null){
                getActionListener().onError(this , new OAuth2Exception("Platform configure is null."));
            }
            return;
        }
        //check url
        String url = getUrl(mConfigure.getAppKey() , mConfigure.getAppSecret() , mConfigure.getRedirectUrl());
        if(TextUtils.isEmpty(url)){
            if(getActionListener() != null){
                getActionListener().onError(this , new OAuth2Exception("Url is empty."));
            }
            return;
        }

        //start OAuth2 UI activity
        Intent intent = new Intent(OAuth2.getContext() , OAuth2UI.class);
        intent.putExtra(OAuth2UI.OAUTH2_URL , url);
        intent.putExtra(OAuth2UI.OAUTH2_REDIRECT_URL , mConfigure.getRedirectUrl());
        PendingIntent pendingIntent = PendingIntent.getActivity(OAuth2.getContext() , 0 ,intent , 0);
        try {
            pendingIntent.send();
        }catch (PendingIntent.CanceledException e){
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            OAuth2.getContext().startActivity(intent);
        }
    }
}
