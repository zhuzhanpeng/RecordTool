package com.onairm.baselibrary.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by android on 2017/3/3.
 */

public class SpUtil {
    private static SharedPreferences sp;
    private static String name = "SharePreference";
    private static int mode = Context.MODE_PRIVATE;
    private static SpUtil sharePreferenceUtil = new SpUtil();

    public static SpUtil init(Context context) {
        sp = context.getSharedPreferences(name, mode);
        return sharePreferenceUtil;
    }


    public static void putPreference(String preferenceName, String preference) {
        sp.edit().putString(preferenceName, preference).commit();
    }

    public static String getStringPreference(String preferenceName) {
        return sp.getString(preferenceName, "");
    }

    public static String getStringPreference(String preferenceName, String defValue) {
        return sp.getString(preferenceName, defValue);
    }

    public static void putPreference(String preferenceName, int preference) {
        sp.edit().putInt(preferenceName, preference).commit();
    }




    //删除某一个值
    public static void clearPreference(String preferenceName) {
        sp.edit().remove(preferenceName).commit();
    }



    public static int getIntPreference(String preferenceName) {
        return sp.getInt(preferenceName, 0);
    }

    public static int getIntPreference(String preferenceName, int defValue) {
        return sp.getInt(preferenceName, defValue);
    }

    public static void putPreference(String preferenceName, long preference) {
        sp.edit().putLong(preferenceName, preference).commit();
    }

    public static long getLongPreference(String preferenceName) {
        return sp.getLong(preferenceName, 0);
    }

    public static long getLongPreference(String preferenceName, long defValue) {
        return sp.getLong(preferenceName, defValue);
    }

    public static void putPreference(String preferenceName, boolean preference) {
        sp.edit().putBoolean(preferenceName, preference).commit();
    }

    public static boolean getBooleanPreference(String preferenceName) {
        return sp.getBoolean(preferenceName, false);
    }

    /**
     * 删除信息
     *
     * @throws Exception
     */
    public static void deleteAll() throws Exception {
        sp.edit().clear();
        sp.edit().commit();
    }

    /**
     * 删除一条信息
     */
    public static void delete(String key) {
        sp.edit().remove(key);
        sp.edit().commit();
    }
}
