package com.onairm.baselibrary.praise;

import com.onairm.baselibrary.net.Api.BaseData;

import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by android on 2017/8/15.
 */

public interface IHttpPraise {
    @POST("news/star")
    Observable<BaseData<PraiseEntity>> praiseNum(@Query("type") String type, @Query("contentId") String contentId);
}
