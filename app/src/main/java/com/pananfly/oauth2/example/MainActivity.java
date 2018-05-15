package com.pananfly.oauth2.example;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.pananfly.oauth2.OAuth2;
import com.pananfly.oauth2.exception.OAuth2Exception;
import com.pananfly.oauth2.platform.Platform;
import com.pananfly.oauth2.platform.PlatformActionListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //init oauth2
        OAuth2.init(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onClick(View v) {
        //init your platform
        Platform platform = new Example();
        //set callback listener
        platform.setActionListener(mListener);
        //start authorize
        platform.autorize();
    }

    private PlatformActionListener mListener = new PlatformActionListener(){
        @Override
        public void onError(Platform platform,final OAuth2Exception exception) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MainActivity.this , "error:" + exception.getMessage() , Toast.LENGTH_LONG).show();
                }
            });
        }

        @Override
        public void onComplete(final Platform platform) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    String result = null;
                    //save result in SharedPreferences
                    if(platform.getPlatformName().equals(Example.class.getSimpleName())) {
                        if (platform.getSp() != null) {
                            result = platform.getSp().getString(Example.RESULT , null);
                        }
                    }
                    Toast.makeText(MainActivity.this , "result:" + result , Toast.LENGTH_LONG).show();
                }
            });
        }

        @Override
        public void onCancel(Platform platform) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MainActivity.this , "authorize canceled!" , Toast.LENGTH_LONG).show();
                }
            });
        }
    };
}
