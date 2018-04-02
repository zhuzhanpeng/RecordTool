package com.onairm.recordtool4android.utils;

import android.content.Context;
import android.os.Environment;
import android.text.format.Formatter;
import android.util.Log;


import com.onairm.recordtool4android.activity.MainActivity;

import java.io.File;

/**
 * Created by apple on 18/1/26.
 */

public class SdcardUtils {
    public static boolean hasUsable(){
        if(!Environment.getExternalStorageState().equals( Environment.MEDIA_MOUNTED)){
            //sdcard状态  无
            return false;
        }
        return true;
    }

    public static boolean hasSpace(Context mContext){
        File sdcard_filedir = Environment.getExternalStorageDirectory();//得到sdcard的目录作为一个文件对象
        long usableSpace = sdcard_filedir.getUsableSpace();//获取文件目录对象剩余空间
        long totalSpace = sdcard_filedir.getTotalSpace();
        //long类型的文件  转化为 M，G字符串
        String usableSpace_str = Formatter.formatFileSize(mContext, usableSpace);
        String totalSpace_str = Formatter.formatFileSize(mContext, totalSpace);
        Log.d("sdcard","sdcard>>>>>>>>>>>>usableSpace>>>>"+ usableSpace_str+">>>>>>totalSpace>>>>"+totalSpace_str);
        if(usableSpace < 1024 * 1024 * 100){ //判断剩余空间是否小于100M
              return false;
        }
        return true;
    }
}
