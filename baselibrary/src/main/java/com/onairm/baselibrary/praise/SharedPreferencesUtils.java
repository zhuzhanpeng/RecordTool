package com.onairm.baselibrary.praise;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.Map;
import java.util.Set;

/**
 * Created by jilong on 16/9/11.
 */
class SharedPreferencesUtils {
    private SharedPreferences sp;
    private Editor editor;
    private String name = "praiseShare";
    private int mode = Context.MODE_PRIVATE;

    public SharedPreferencesUtils(Context context) {
        this.sp = context.getSharedPreferences(name, mode);
        this.editor = sp.edit();
    }

    /**
     * 创建一个工具类，默认打开名字为name的SharedPreferences实例
     * @param context
     * @param name   唯一标识
     * @param mode   权限标识
     */
    public SharedPreferencesUtils(Context context, String name, int mode) {
        this.sp = context.getSharedPreferences(name, mode);
        this.editor = sp.edit();
    }

    /**
     * 添加信息到SharedPreferences
     *
     * @param map
     * @throws Exception
     */
    public void add(Map<String, String> map) {
        Set<String> set = map.keySet();
        for (String key : set) {
            editor.putString(key, map.get(key));
        }
        editor.commit();
    }

    /**
     * 删除信息
     *
     * @throws Exception
     */
    public void deleteAll() throws Exception {
        editor.clear();
        editor.commit();
    }

    /**
     * 删除一条信息
     */
    public void delete(String key){
        editor.remove(key);
        editor.commit();
    }

    /**
     * 获取信息
     *
     * @param key
     * @return
     * @throws Exception
     */
    public String get(String key){
        if (sp != null) {
            return sp.getString(key, "");
        }
        return "";
    }

    /**
     * 获取此SharedPreferences的Editor实例
     * @return
     */
    public Editor getEditor() {
        return editor;
    }


}
