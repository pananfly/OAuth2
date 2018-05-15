package com.pananfly.oauth2.example;

import com.pananfly.oauth2.exception.OAuth2Exception;
import com.pananfly.oauth2.platform.Platform;

public class Example extends Platform {
    private int id = 0;
    public static final String RESULT = "result";

    @Override
    public String getPlatformName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public int getPlatformId() {
        return id;
    }

    @Override
    public void authorizeResult(String result) {
        //Do your own things
        //.....

        //for example
        boolean error = false;
        if(error && getActionListener() != null){
            getActionListener().onError(this , new OAuth2Exception("Something wrong."));
            return;
        }

        //save result in SharedPreferences
        if(getSp() != null){
            getSp().putString(RESULT , result);
        }

        //callback complete
        if(getActionListener() != null){
            getActionListener().onComplete(this);
        }
    }

    @Override
    public String getUrl(String appKey, String appSecret, String redirectUrl) {
        return "http://m.baidu.com/s?word=example";
    }
}
