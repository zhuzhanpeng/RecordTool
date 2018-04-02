package com.onairm.recordtool4android.bean;

import android.graphics.drawable.Drawable;

/**
 * Created by apple on 18/1/25.
 */

public class MyAppInfo {
    private Drawable image;
    private String appName;
    //应用包名
    private String packagename;


    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPackagename() {
        return packagename;
    }

    public void setPackagename(String packagename) {
        this.packagename = packagename;
    }
}
