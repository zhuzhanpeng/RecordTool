package com.onairm.baselibrary.praise;

import android.content.Context;
import android.widget.TextView;

import com.onairm.baselibrary.net.Api.BaseData;
import com.onairm.baselibrary.utils.TipToast;
import com.onairm.baselibrary.utils.Utils;

/*;*/

/**
 * 点赞策略一
 */
class PraiseStrategy implements StarManager {
    private final boolean isJudgeLogin = false;

    public static PraiseStrategy getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final PraiseStrategy INSTANCE = new PraiseStrategy();
    }

    /**
     * @param entity            实体
     * @param starSuccess       点赞成功
     * @param starCancelSuccess 取消点赞成功  没有取消点赞时 传null
     */
    public void executeStar(Context context, final PraiseEntity entity
            , final PraiseHelper.StarSuccess starSuccess
            , final PraiseHelper.StarCancelSuccess starCancelSuccess) {
        if (null == entity)
            return;
        final String key = PraiseHelper.getKey(entity.type, entity.objectId);
        //检查网络
        if (Utils.isNetAvailable()) {
        } else {
            TipToast.shortTip("请检查网络");
            return;
        }
        //判断点赞是否需要登录
        /*if(isJudgeLogin){
            if (!DemoHXSDKHelper.getInstance().isLogined()) {
                entity.activity.get().startActivity(new Intent(entity.activity.get(), LoginActivity.class));
                return;
            }else{
                //登录的情况
            }
        }*/

        if (PraiseHelper.canNext(key)) {
            PraiseHelper.setFinishedFalse(key);
            if (isPraise(key)) {
                //点赞
                PraiseHelper.star(context, entity.objectId, entity.type, new PraiseHelper.StarSuccess() {
                    @Override
                    public void starSuccess(PraiseEntity praiseData) {
                        if (null != entity.tvStar && null != entity.tvStar.get()) {
                            entity.tvStar.get().setSelected(true);
                            TextView tvStar = entity.tvStar.get();
                            String strCount = tvStar.getText().toString().trim();
                            long preCount = 0;
                            try {
                                preCount = Long.parseLong(strCount);
                                entity.tvStar.get().setText(PraiseHelper.showRule(preCount + 1));
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }
                            PraiseHelper.StarEntity starEntity = PraiseHelper.getStarEntity(key);
                            //增加数
                            starEntity.starTotal = preCount + 1;
                            //更新状态
                            starEntity.status = true;
                            PraiseHelper.saveStarEntity(key, starEntity);
                        }
                        if (null != starSuccess) {
                            starSuccess.starSuccess(praiseData);
                        }
                    }
                });
            } else {
                //取消点赞
                       /* PraiseHelper.cancelStar(entity.objectId, entity.type, new PraiseHelper.StarCancelSuccess() {
                            @Override
                            public void starCancelSuccess(BaseEntity baseEntity) {


                                if (null != entity.tvStar && null != entity.tvStar.get()) {
                                    entity.tvStar.get().setSelected(false);

                                    TextView tvStar = entity.tvStar.get();
                                    String strCount = tvStar.getText().toString().trim();
                                    long currentCount = 0;
                                    try {
                                        long preCount = Long.parseLong(strCount);
                                        currentCount = (preCount - 1 < 0) ? 0 : (preCount - 1);
                                        entity.tvStar.get().setText(Utils.countShowRules(currentCount));
                                    } catch (NumberFormatException e) {
                                        e.printStackTrace();
                                    }
                                    PraiseHelper.StarEntity starEntity = PraiseHelper.getStarEntity(key);

                                    //starEntity.minus1();
                                    starEntity.starTotal = currentCount;

                                    //更新状态
                                    starEntity.status = false;
                                    PraiseHelper.saveStarEntity(key, starEntity);

                                }
                                if (null != starCancelSuccess) {
                                    starCancelSuccess.starCancelSuccess(baseEntity);
                                }
                            }
                        });*/
                /**
                 * 1.提示已经点过赞
                 * 2.取消点赞
                 * */
                TipToast.shortTip("已经赞过");
            }
        } else {
            //上次点赞，没有返回
        }
    }
    public boolean isPraise(String key){
//        return !PraiseHelper.isStar(key);
        return true;
    }
}
