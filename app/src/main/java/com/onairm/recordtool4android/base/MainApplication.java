package com.onairm.recordtool4android.base;

import android.app.Application;

import com.onairm.baselibrary.Init;

/**
 * Created by Edison on 2018/1/19.
 */

public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Init.getInstance().init(getApplicationContext());
    }
}
