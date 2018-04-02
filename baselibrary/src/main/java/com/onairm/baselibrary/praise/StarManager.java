package com.onairm.baselibrary.praise;

import android.content.Context;

/**
 * Created by Edison on 2017/1/5.
 */

public interface StarManager {
    //判断是否点赞
    //获取点赞数
    //点赞动作
    void executeStar(Context context, final PraiseEntity entity
            , final PraiseHelper.StarSuccess starSuccess
            , final PraiseHelper.StarCancelSuccess starCancelSuccess);
    //取消点赞
}
