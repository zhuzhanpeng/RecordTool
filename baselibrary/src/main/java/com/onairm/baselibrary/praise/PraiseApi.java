package com.onairm.baselibrary.praise;

import com.onairm.baselibrary.net.Api.BaseApi;
import com.onairm.baselibrary.net.Api.BaseData;
import com.onairm.baselibrary.net.listener.HttpOnNextListener;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import retrofit2.Retrofit;
import rx.Observable;

/**
 * Created by android on 2017/8/15.
 */

public class PraiseApi extends BaseApi {

    private String type;
    private String contentId;

    public PraiseApi(String type, String contentId, HttpOnNextListener listener, RxAppCompatActivity rxAppCompatActivity) {
        super(listener, rxAppCompatActivity);
        setShowProgress(true);
        setCancel(true);
        setCache(false);
        this.type = type;
        this.contentId = contentId;
    }

    @Override
    public Observable getObservable(Retrofit retrofit) {
        IHttpPraise iHttpPraise = retrofit.create(IHttpPraise.class);
        return iHttpPraise.praiseNum(type, contentId);
    }
}
