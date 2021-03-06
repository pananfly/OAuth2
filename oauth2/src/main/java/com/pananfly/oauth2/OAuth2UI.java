package com.pananfly.oauth2;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.pananfly.oauth2.exception.OAuth2Exception;

/**
 * OAuth2 Webview authorize
 * @author pananfly
 */
public class OAuth2UI extends AppCompatActivity {

    public static final String OAUTH2_URL = "oauth2_url";
    public static final String OAUTH2_REDIRECT_URL = "oauth2_redirect_url";
    public static final String OAUTH2_METADATA_NAME = "com.pananfly.oauth2.REMOVE_COOKIE";
    private LinearLayout rootView;
    private WebView mWebView;
    private String url;
    private String redirectUrl;
    private volatile boolean isAuthorized = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        url = getIntent().getStringExtra(OAUTH2_URL);
        if(url == null){
            //error
            if (OAuth2.getOAuth2Listener() != null) {
                OAuth2.getOAuth2Listener().onError(new OAuth2Exception("Url is empty."));
            }
            finish();
            return;
        }
        //get activity meta-data value
        boolean isRemoveCookie = getBooleanMetaData(OAUTH2_METADATA_NAME , false);
        if(isRemoveCookie) {
            removeCookie(this);
        }
        redirectUrl = getIntent().getStringExtra(OAUTH2_REDIRECT_URL);

        //init view
        rootView = new LinearLayout(this );
        rootView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.FILL_PARENT));
        mWebView = new WebView(this);
        mWebView.setHorizontalScrollBarEnabled(false);
        mWebView.setVerticalScrollBarEnabled(false);
        mWebView.setWebViewClient(new OAuth2WebViewClient());
        mWebView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.FILL_PARENT));
        mWebView.requestFocus();
        WebSettings settings = mWebView.getSettings();
//        settings.setJavaScriptEnabled(true);
        settings.setSavePassword(false);
        settings.setDisplayZoomControls(false);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setDefaultTextEncodingName("UTF-8");
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        //display adaptation
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int mDensity = metrics.densityDpi;
        if (mDensity == 240) {
            settings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        } else if (mDensity == 160) {
            settings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        } else if(mDensity == 120) {
            settings.setDefaultZoom(WebSettings.ZoomDensity.CLOSE);
        }else if(mDensity == DisplayMetrics.DENSITY_XHIGH){
            settings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        }else if (mDensity == DisplayMetrics.DENSITY_TV){
            settings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        }else{
            settings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            settings.setLoadsImagesAutomatically(true);
        } else {
            settings.setLoadsImagesAutomatically(false);
        }

        mWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                view.requestFocus();
            }
        });

        rootView.addView(mWebView);
        setContentView(rootView);

        mWebView.loadUrl(url);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        rootView.removeView(mWebView);
        if(mWebView != null) {
            mWebView.removeAllViews();
            mWebView.destroy();
        }
        mWebView = null;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(mWebView != null && mWebView.canGoBack()){
                mWebView.goBack();
                return true;
            }
            //cancel
            if (OAuth2.getOAuth2Listener() != null) {
                OAuth2.getOAuth2Listener().onCancle();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private class OAuth2WebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            if(!TextUtils.isEmpty(redirectUrl) && url.startsWith(redirectUrl) && !isAuthorized){
                //complete
                isAuthorized = true;
                if(OAuth2.getOAuth2Listener() != null){
                    OAuth2.getOAuth2Listener().onComplete(url);
                }
                finish();
            }
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
//            super.onReceivedSslError(view, handler, error);
            handler.proceed();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            view.getSettings().setLoadsImagesAutomatically(true);
        }
    }

    /**
     * Remove cookie
     *
     * @param context
     */
    private void removeCookie(Context context) {
        CookieSyncManager.createInstance(context);
        CookieSyncManager.getInstance().startSync();
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.removeSessionCookie();
    }

    /**
     * Get activity boolean meta-data
     * @return
     */
    private boolean getBooleanMetaData(String name , boolean defaultValue){
        try{
            ActivityInfo activityInfo = getPackageManager().getActivityInfo(getComponentName(), PackageManager.GET_META_DATA);
            return activityInfo.metaData.getBoolean(name , defaultValue);
        }catch (PackageManager.NameNotFoundException e){
            e.printStackTrace();
        }catch (NullPointerException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        return defaultValue;
    }
}
