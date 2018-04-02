package com.onairm.baselibrary.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.onairm.baselibrary.gson.DoubleDefault0Adapter;
import com.onairm.baselibrary.gson.IntegerDefault0Adapter;
import com.onairm.baselibrary.gson.LongDefault0Adapter;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import java.lang.reflect.Type;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

public class GsonUtil {
    private static Gson gson = null;
    static {
        if (gson == null) {
            gson = new Gson();
        }
    }

    private GsonUtil() {
    }

    public static <T> T fromJson(String jsonString, Class<T> cls) {
        T t = null;
        try {
            if (gson != null) {
                t = buildGson().fromJson(jsonString, cls);
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return t;
    }

    public static <T> List<T> jsonToList(String json, Class<T> classOfT) {
        List<T> t=null;
        try {
            if (gson != null) {
                Type type = new TypeToken<ArrayList<T>>() {}.getType();
                t = buildGson().fromJson(json, type);
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return t;
    }

    public static String toJson(Object obj){
        String str = "";
        try {
            if (gson != null) {
                str = gson.toJson(obj);
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return str;
    }

    public static <T> T jsonData(String str, Type type){
        T t = null;
        try {
            if (gson != null) {
                t = buildGson().fromJson(str,type);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }

    /**
     * 增加后台返回""和"null"的处理
     * 1.int=>0
     * 2.double=>0.00
     * 3.long=>0L
     *
     * @return
     */
    public static Gson buildGson() {
        if (gson == null) {
            gson = new GsonBuilder()
                    .registerTypeAdapter(Integer.class, new IntegerDefault0Adapter())
                    .registerTypeAdapter(int.class, new IntegerDefault0Adapter())
                    .registerTypeAdapter(Double.class, new DoubleDefault0Adapter())
                    .registerTypeAdapter(double.class, new DoubleDefault0Adapter())
                    .registerTypeAdapter(Long.class, new LongDefault0Adapter())
                    .registerTypeAdapter(long.class, new LongDefault0Adapter())
                    .create();
        }
        return gson;
    }

    // 将Json数组解析成相应的映射对象列表
    public static <T> List<T> jsonToList(Class<T> myClass, String jsonStr){
        List<T> t = null;
        try {
            if (gson != null) {
                Type type = new ListParameterizedType(myClass);
                t = buildGson().fromJson(jsonStr, type);
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return t;
    }

    private static class ListParameterizedType implements ParameterizedType {
        private Type type;
        private ListParameterizedType(Type type) {
            this.type = type;
        }
        @Override
        public Type[] getActualTypeArguments() {
            return new Type[] {type};
        }
        @Override
        public Type getRawType() {
            return ArrayList.class;
        }
        @Override
        public Type getOwnerType() {
            return null;
        }
        // implement equals method too! (as per javadoc)
    }
}