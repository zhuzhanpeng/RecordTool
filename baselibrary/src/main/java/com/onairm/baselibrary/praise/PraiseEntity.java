package com.onairm.baselibrary.praise;

import android.widget.TextView;

import java.lang.ref.WeakReference;

public class PraiseEntity {
    public String objectId;
    public long starTotal;//点赞数
    public int type;//区分点赞类型 1.图片 2.评论
    public WeakReference<TextView> tvStar;//显示点赞数的TextView
//    public WeakReference<TextView> tvStar;//显示点赞数的TextView
    //    public String key;//objectId+分类
    //    public long timestamp;//查询今天是否点赞用的时间戳
    //    public WeakReference<Activity> activity;//如果没有登录，跳转登录界面用的
}