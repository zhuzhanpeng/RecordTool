package com.onairm.baselibrary.praise;

import android.content.Context;

import com.onairm.baselibrary.Init;
import com.onairm.baselibrary.net.Api.BaseData;
import com.onairm.baselibrary.net.listener.HttpOnNextListener;
import com.onairm.baselibrary.utils.SerializableUtil;
import com.onairm.baselibrary.utils.SpUtil;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 点赞帮助类
 * 策略：1本地保存点赞数
 *      2点赞一次，本地加1
 *      3如果本地点赞数为0，把服务器点赞数保存到本地
 *      4读取是，比较服务器和本地，谁大用谁
 *      5点赞后，key对应value变为true
 */

/**
 * 代码：1.读取时，比较服务器和本地，谁大取谁
 *      2.点赞完成，更新之前的页面
 * */
public class PraiseHelper {
    /**
     * type:1:文章  2:评论 3:生活 4:自媒体文章 5:问题 6:回答 7:帖子
    */
    public static final int TYPE_NEWS = 1;
    public static final int TYPE_COMMENT = 2;
    public static final int TYPE_ACT = 3;
    public static final int TYPE_MEDIA = 4;
    public static final int TYPE_QUESTION = 5;
    public static final int TYPE_ANSWER = 6;
    public static final int TYPE_NOTE = 7;

    public static final String KEY_PRAISE = "praise";
    /**
     * 读取数值的规则
     * */
    public static String getStarCount(String key, long serverStarTotal) {
        long result = 0;
        StarEntity starEntity = getStarEntity(key);
        if(starEntity.starTotal==0){
            result = serverStarTotal;
            //如果本地点赞为0，把服务器的点赞数保存到本地
            starEntity.starTotal = serverStarTotal;
            PraiseHelper.saveStarEntity(key, starEntity);
            setFinishedTrue(key);
        }else{
            //谁大取谁
            result = (starEntity.starTotal>serverStarTotal) ? starEntity.starTotal : serverStarTotal;
        }
        return showRule(result);
    }

    public static void setFinishedTrue(String key) {
        StarEntity starEntity = getStarEntity(key);
        starEntity.finished = true;
        saveStarEntity(key, starEntity);
    }

    public static void setFinishedFalse(String key) {
        StarEntity starEntity = getStarEntity(key);
        starEntity.finished = false;
        saveStarEntity(key, starEntity);
    }

    public static boolean isStar(String key) {
        Map<String, StarEntity> map = getMap();
        if (map.containsKey(key)) {
            return map.get(key).status;
        } else {
            return false;
        }
    }
    public static boolean canNext(String id) {
        Map<String, StarEntity> map = getMap();
        if (map.containsKey(id)) {
            return map.get(id).finished;
        } else {
            return true;
        }
    }



    public static StarEntity getStarEntity(String id) {
        Map<String, StarEntity> starMap = getMap();
        StarEntity starEntity = starMap.get(id);
        if (null == starEntity) {
            starEntity = new StarEntity();
        }
        return starEntity;
    }

    public static void saveStarEntity(String id, StarEntity starEntity) {
        Map<String, StarEntity> starMap = getMap();
        starMap.put(id, starEntity);
        SharedPreferencesUtils sp = new SharedPreferencesUtils(Init.context);
        Map<String, String> stringMap = new HashMap<>();
        String idTimeString = SerializableUtil.serialize(starMap);
        stringMap.put(KEY_PRAISE, idTimeString);
        sp.add(stringMap);
    }

    private static Map<String, StarEntity> getMap() {
        Map<String, StarEntity> starMap;
        SharedPreferencesUtils sp = new SharedPreferencesUtils(Init.context);
        String mapStr = sp.get(KEY_PRAISE);
        starMap = SerializableUtil.deSerialization(mapStr);
        if (starMap == null)
            starMap = new HashMap<>();

        return starMap;
    }





    /*public static void cancelStar(final int objectId, int type, final StarCancelSuccess starCancelSuccess) {
        String uid = MainApplication.getInstance().getUserId();
        if (uid == null)
            uid = "-1";
        final String key = type + "" + objectId;
        Map<String, String> map = new HashMap<>();
        map.putAll(NetUtils.getPostMap(Init.getContext()));
        map.put("userId", uid);//1
        map.put("objectId", objectId + "");
        map.put("type", type + "");
        NetUtils.HttpPost(map, NetApi.DELETE_STAR, new HttpCallback<String>() {
            @Override
            public void onError() {
                TipToast.shortTip("网络请求失败");
                StarImplUtils.setFinishedTrue(key);
            }

            @Override
            public void onResponse(String response) {
                if (response != null) {
                    BaseEntity baseEntity = GsonUtil.getPerson(response, BaseEntity.class);
                    if (null != starCancelSuccess) {
                        starCancelSuccess.starCancelSuccess(null);
                    }
                }
                StarImplUtils.setFinishedTrue(key);
            }
        });

    }
*/

    public interface StarSuccess {
        void starSuccess(PraiseEntity praiseData);
    }

    public interface StarCancelSuccess {
        void starCancelSuccess(PraiseEntity praiseData);
    }

    //删除所有的点赞记录
    public static void clearZanCache() {
//        SharedPreferencesUtils sp = new SharedPreferencesUtils(Init.context);
//        sp.delete(KEY_PRAISE);
        SpUtil.delete(KEY_PRAISE);
    }

    /**
     * type:1:文章  2:评论 3:生活 4:自媒体文章 5:问题 6:回答 7:帖子
     */
    public static void star(Context context, final String objectId, final int type, final StarSuccess starSuccess) {
//        NetRequest.PostRequest(Config.REQUEST_GOOD, PraiseData.class, new HttpHandler<PraiseData>() {
//            @Override
//            public void reqSuccess(PraiseData response) {
//                if (response != null) {
//                    if (starSuccess != null) {
//                        starSuccess.starSuccess(response);
//                    }
//                }
//                setFinishedTrue(getKey(type,objectId));
//            }
//
//            @Override
//            public void reqFail(PraiseData response) {
//
//            }
//
//            @Override
//            public void reqError(VolleyError error) {
//                if (error instanceof NetworkError){
//                    TipToast.shortTip(R.string.net_error);
//                    setFinishedTrue(getKey(type,objectId));
//                }
//            }
//
//            @Override
//            public Map<String, String> getsParams() {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put(Config.UID, SharePreferences.getUserId());
//                params.put(Config.TYPE, String.valueOf(type));
//                params.put("contentId", objectId+"");
//                return params;
//            }
//        });

        Init.httpManager.doHttpDeal(new PraiseApi(String.valueOf(type), objectId + "", new HttpOnNextListener<PraiseEntity>() {

            @Override
            public void onNext(PraiseEntity praiseEntity) {
                if (praiseEntity != null) {
                    if (starSuccess != null) {
                        starSuccess.starSuccess(praiseEntity);
                    }
                }
                setFinishedTrue(getKey(type,objectId));
            }
        }, (RxAppCompatActivity)context));
    }
    /**
     * 数量的显示规则
     * */
    public static String showRule(long count){
        return count+"";
    }
    public static String getKey(int type,int objectId){
        String key = type + "" + objectId;
        return key;
    }
    public static String getKey(int type,String objectId){
        String key = type + "" + objectId;
        return key;
    }
    public static String getKey(String type,String objectId){
        String key = type + "" + objectId;
        return key;
    }
    public static class StarEntity implements Serializable {
        public long starTotal;//点赞数
        protected boolean status;//true:点赞，false:取消
        protected boolean finished;//一次点赞是否完成。true:完成。false:未完成。默认false
    }
}
