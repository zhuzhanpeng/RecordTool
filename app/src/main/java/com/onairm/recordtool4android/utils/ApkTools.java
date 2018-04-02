package com.onairm.recordtool4android.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import com.onairm.recordtool4android.bean.MyAppInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apple on 18/1/25.
 */

public class ApkTools {
    private Context context;
    private PackageManager pm;


    public ApkTools(Context context) {
        this.context = context;
        pm = context.getPackageManager();
    }

    /**
     * 得到手机中所有的应用程序信息
     * @return
     */
    public List<MyAppInfo> getAppInfos(){
        //创建要返回的集合对象
        List<MyAppInfo> appInfos = new ArrayList<MyAppInfo>();
        //获取手机中所有安装的应用集合
        List<ApplicationInfo> applicationInfos = pm.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
        //遍历所有的应用集合
        for(ApplicationInfo info : applicationInfos){

            MyAppInfo appInfo = new MyAppInfo();

            //获取应用程序的图标
            Drawable app_icon = info.loadIcon(pm);
            appInfo.setImage(app_icon);

            //获取应用的名称
            String app_name = info.loadLabel(pm).toString();
            appInfo.setAppName(app_name);

            //获取应用的包名
            String packageName = info.packageName;
            appInfo.setPackagename(packageName);



        }
        return appInfos;
    }
}
