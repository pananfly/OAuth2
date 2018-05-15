package com.pananfly.oauth2.platform;

import android.app.PendingIntent;
import android.content.Intent;

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
        public void onComplete(String result) {
            authorizeResult(result);
        }
    };

    /**
     * Ger platform name
     * @return
     */
    public abstract String getPlatformName();

    public abstract int getPlatformId();

    public abstract void authorizeResult(String result);

    /**
     * Get oauth access url
     * @param appKey
     * @param appSecret
     * @param redirectUrl
     * @return
     */
    public abstract String getUrl(String appKey , String appSecret , String redirectUrl);

    public OAuth2SharePreference getSp(){
        return mSp;
    }

    public String getAppKey(){
        if(mConfigure == null){
            return null;
        }
        return mConfigure.getAppKey();
    }

    public String getAppSecret(){
        if(mConfigure == null){
            return null;
        }
        return mConfigure.getAppSecret();
    }

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

    protected PlatformActionListener getActionListener(){
        return mListener;
    }

    public void autorize(){
        if(mConfigure == null){
            if(getActionListener() != null){
                getActionListener().onError(this , new OAuth2Exception("Platform configure is null"));
            }
            return;
        }
        Intent intent = new Intent(OAuth2.getContext() , OAuth2UI.class);
        intent.putExtra(OAuth2UI.OAUTH2_URL , getUrl(mConfigure.getAppKey() , mConfigure.getAppSecret() , mConfigure.getRedirectUrl() ));
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
