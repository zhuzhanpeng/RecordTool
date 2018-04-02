package com.onairm.recordtool4android.utils;

import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Build;

import com.onairm.baselibrary.utils.TipToast;

/**
 * 适配魅族5.0的录屏问题
 */

public class MeiZuPermissionUtil {
    public static void checkMeizuCameraPermission(Context context) {
        final int version = Build.VERSION.SDK_INT;
        final String phoneName=getDeviceBrand();
        if (version >= 19 && version<23 && "Meizu".equals(phoneName)) {
            if(!cameraIsCanUse()){
                TipToast.shortTip("魅族手机必须打开拍照和录像权限");
                applyPermission(context);
            }
        }

    }
    private static boolean cameraIsCanUse() {
        boolean isCanUse = true;
        Camera mCamera = null;
        try {
            mCamera = Camera.open();
            Camera.Parameters mParameters = mCamera.getParameters(); //针对魅族手机
            mCamera.setParameters(mParameters);
        } catch (Exception e) {
            isCanUse = false;
        }

        if (mCamera != null) {
            try {
                mCamera.release();
            } catch (Exception e) {
                e.printStackTrace();
                return isCanUse;
            }
        }
        return isCanUse;
    }

    /**
     * 去魅族权限申请页面
     */
    private static void applyPermission(Context context){
        Intent intent = new Intent("com.meizu.safe.security.SHOW_APPSEC");
        intent.setClassName("com.meizu.safe", "com.meizu.safe.security.AppSecActivity");
        intent.putExtra("packageName", context.getPackageName());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
    /**
     * 获取手机厂商
     *
     * @return  手机厂商
     */
    public static String getDeviceBrand() {
        return android.os.Build.BRAND;
    }
}
