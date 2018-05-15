package com.pananfly.oauth2;

import android.content.Context;
import android.support.annotation.NonNull;

import java.io.IOException;

/**
 * OAuth2
 * @author pananfly
 */
public class OAuth2 {
    private static Context mContext;
    private static OAuth2Listener mListener = null;

    public static synchronized void init(@NonNull Context context){
        if(mContext == null){
            mContext = context.getApplicationContext();
            try {
                OAuth2Configures.readConfigures(mContext.getAssets().open("OAuth2.xml"));
            }catch (IOException e){

            }
        }
    }

    public static Context getContext(){
        return mContext;
    }

    public static void registerOAuth2Listener(OAuth2Listener listener){
        mListener = listener;
    }

    public static OAuth2Listener getOAuth2Listener(){
        return mListener;
    }
}
