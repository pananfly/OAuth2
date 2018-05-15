package com.pananfly.oauth2;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

/**
 * OAuth2 save key-value
 * @author pananfly
 */
public class OAuth2SharePreference{
    private SharedPreferences mSp;
    private final String name = "OAuth2_sp";
    public OAuth2SharePreference(@NonNull Context context){
        mSp = context.getSharedPreferences(name , Context.MODE_PRIVATE);
    }

    public boolean putString(String key , String value){
        if(mSp != null){
            SharedPreferences.Editor editor = mSp.edit();
            editor.putString(key, value);
            return editor.commit();
        }
        return false;
    }

    public boolean putInt(String key , int value){
        if(mSp != null){
            SharedPreferences.Editor editor = mSp.edit();
            editor.putInt(key, value);
            return editor.commit();
        }
        return false;
    }

    public boolean putLong(String key , long value){
        if(mSp != null){
            SharedPreferences.Editor editor = mSp.edit();
            editor.putLong(key, value);
            return editor.commit();
        }
        return false;
    }

    public boolean putBoolean(String key , boolean value){
        if(mSp != null){
            SharedPreferences.Editor editor = mSp.edit();
            editor.putBoolean(key, value);
            return editor.commit();
        }
        return false;
    }

    public String getString(String key , String defaultValue){
        if(mSp != null){
           return mSp.getString(key, defaultValue);
        }
        return defaultValue;
    }

    public int getInt(String key , int defaultValue){
        if(mSp != null){
            return mSp.getInt(key, defaultValue);
        }
        return defaultValue;
    }

    public long getLong(String key , long defaultValue){
        if(mSp != null){
            return mSp.getLong(key, defaultValue);
        }
        return defaultValue;
    }

    public boolean getBoolean(String key , boolean defaultValue){
        if(mSp != null){
            return mSp.getBoolean(key, defaultValue);
        }
        return defaultValue;
    }


}
