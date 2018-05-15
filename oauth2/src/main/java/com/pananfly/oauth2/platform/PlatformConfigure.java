package com.pananfly.oauth2.platform;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * xml platform configure item
 * @author pananfly
 */
public class PlatformConfigure implements Parcelable {
    private int id;
    private String name;
    private String appKey;
    private String appSecret;
    private String redirectUrl;

    public PlatformConfigure(int id, String name, String appKey, String appSecret, String redirectUrl) {
        this.id = id;
        this.name = name;
        this.appKey = appKey;
        this.appSecret = appSecret;
        this.redirectUrl = redirectUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.appKey);
        dest.writeString(this.appSecret);
        dest.writeString(this.redirectUrl);
    }

    public PlatformConfigure() {
    }

    protected PlatformConfigure(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.appKey = in.readString();
        this.appSecret = in.readString();
        this.redirectUrl = in.readString();
    }

    public static final Creator<PlatformConfigure> CREATOR = new Creator<PlatformConfigure>() {
        @Override
        public PlatformConfigure createFromParcel(Parcel source) {
            return new PlatformConfigure(source);
        }

        @Override
        public PlatformConfigure[] newArray(int size) {
            return new PlatformConfigure[size];
        }
    };
}
