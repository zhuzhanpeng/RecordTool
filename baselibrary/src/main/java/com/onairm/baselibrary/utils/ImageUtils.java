package com.onairm.baselibrary.utils;

import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import com.onairm.baselibrary.Init;
import com.squareup.picasso.Picasso;

/**
 * Created by android on 2017/8/16.
 */

public class ImageUtils {

    private static String qNiuHost;
    private static int roundHeadholder;
    private static int rectHeadholder;
    private static int placeholder;


    public static String getQiNiuHost(){
        if(!TextUtils.isEmpty(qNiuHost)){
            return qNiuHost;
        }else{
            return SpUtil.getStringPreference("qiNiuHost");
        }
    }
    public static String getUrl(String str) {
//        if (SharePreferences.isSaveFlow() && !Tools.isWifi()) { //非wifi无图模式
//            return null;
//        }
        if (str != null && !str.equals("")) {
            if (str.contains("http")) {
                return str;
            } else {
                return "http://" + getQiNiuHost() + "/" + str;
            }
        } else {
            return null;
        }
    }

    public static void showImage(String url, ImageView imageView) {
        if (placeholder != 0 && getQiNiuHost() != null)
            Picasso.with(Init.getInstance().context)
                    .load(getUrl(url))
                    .placeholder(getPlaceholder())
                    .error(getPlaceholder())
                    .into(imageView);

        Log.d("Image", "showImage: "+getUrl(url));
    }

    public static void showImage(String url, ImageView imageView, int placeholder) {
        if (placeholder != 0 && getQiNiuHost() != null)
            Picasso.with(Init.getInstance().context)
                    .load(getUrl(url))
                    .placeholder(placeholder)
                    .error(placeholder)
                    .into(imageView);

        Log.d("Image", "showImage: "+getUrl(url));
    }






    public static void setqNiuHost(String qNiuHost) {
        ImageUtils.qNiuHost = qNiuHost;
    }

    public static int getRoundHeadholder() {
        return roundHeadholder;
    }

    public static void setRoundHeadholder(int roundHeadholder) {
        ImageUtils.roundHeadholder = roundHeadholder;
    }

    public static int getRectHeadholder() {
        return rectHeadholder;
    }

    public static void setRectHeadholder(int rectHeadholder) {
        ImageUtils.rectHeadholder = rectHeadholder;
    }

    public static int getPlaceholder() {
        return placeholder;
    }
    public static void setPlaceholder(int placeholder) {
        ImageUtils.placeholder = placeholder;
    }



    public static String getUserHeadImage(){
        return "?imageView2/2/w/100/h/100";
    }

    public static String getTopicDetailImage(){
        return "?imageView2/2/w/166/h/166";
    }

    public static String getMovieDetailImage(){
        return "?imageView2/2/w/240/h/356";
    }

    public static String getContentBigImage(){
        return "?imageView2/2/w/400/h/226";
      //  return "?imageMogr2/gravity/Center/crop/200x113/interlace";
    }

    public static String getShareClipParam(){
        return "?imageView2/2/w/160/h/160";
    }

}
