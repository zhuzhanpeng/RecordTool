package com.onairm.baselibrary;

import android.app.Application;
import android.content.Context;

import com.onairm.baselibrary.net.RxRetrofitApp;
import com.onairm.baselibrary.net.http.HttpManager;
import com.onairm.baselibrary.utils.CrashHandler;
import com.onairm.baselibrary.utils.SpUtil;
import com.onairm.baselibrary.utils.TipToast;
import com.onairm.baselibrary.utils.Utils;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;

/**
 * Created by Edison on 2017/2/23.
 */

public class Init {
    public final static String TAG = "default_tag";
    public static final String TY = "a";


    private volatile static Init init;
    public static HttpManager httpManager;

    //通过方法得到
    public static String version;
    public static String deviceId;

    //必须从外部传进来
    public static Context context;
    public static String baseUrl;
    public static String token;
    public static String userId;

    private boolean isDebug = true;//正式环境下为false，测试环境为true

    private Init() {
    }

    public static Init getInstance() {
        if (init == null) {
            synchronized (Init.class) {
                if (init == null) {
                    init = new Init();
                }
            }
        }
        return init;
    }

    public void init(Context context) {
        this.context = context;
        TipToast.init(context);
        SpUtil.init(context);

        RxRetrofitApp.init((Application) context, isDebug());
        CrashHandler.getInstance().init(context, "base");
        if (isDebug()) {
            Logger.init(TAG).logLevel(LogLevel.FULL);
        } else {
            Logger.init(TAG).logLevel(LogLevel.NONE);
        }
        deviceId = Utils.getDeviceId();
        version = Utils.getVersion();
        httpManager = HttpManager.getInstance();
    }



    public void setParams (String baseUrl, String token, String userId){
        this.baseUrl = baseUrl;
        this.token = token;
        this.userId = userId;
        httpManager.setBaseUrl(getBaseUrl());
        httpManager.setParams(TY, Utils.getVersion(), deviceId, getToken(), getUserId());
    }

    public boolean isDebug() {
        return isDebug;
    }

    public void setDebug(boolean debug) {
        isDebug = debug;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}
