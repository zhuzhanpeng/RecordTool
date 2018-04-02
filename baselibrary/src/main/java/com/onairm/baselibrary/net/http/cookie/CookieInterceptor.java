package com.onairm.baselibrary.net.http.cookie;

import com.onairm.baselibrary.net.utils.CookieDbUtil;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
 * gson持久化截取保存数据
 * Created by WZG on 2016/10/20.
 */
public class CookieInterceptor implements Interceptor {
    private CookieDbUtil dbUtil;
    /*是否缓存标识*/
    private boolean cache;
    /*url*/
    private String url;

    public CookieInterceptor(boolean cache, String url) {
        dbUtil=CookieDbUtil.getInstance();
        this.url=url;
        this.cache=cache;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        // 新的请求,添加参数
        Request newRequest = addParam(request);

        Response response = chain.proceed(newRequest);
        if(cache){
            ResponseBody body = response.body();
            BufferedSource source = body.source();
            source.request(Long.MAX_VALUE); // Buffer the entire body.
            Buffer buffer = source.buffer();
            Charset charset = Charset.defaultCharset();
            MediaType contentType = body.contentType();
            if (contentType != null) {
                charset = contentType.charset(charset);
            }
            String bodyString = buffer.clone().readString(charset);
            CookieResulte resulte= dbUtil.queryCookieBy(url);
            long time=System.currentTimeMillis();
            /*保存和更新本地数据*/
            if(resulte==null){
                resulte  =new CookieResulte(url,bodyString,time);
                dbUtil.saveCookie(resulte);
            }else{
                resulte.setResulte(bodyString);
                resulte.setTime(time);
                dbUtil.updateCookie(resulte);
            }
        }
        return response;
    }

    /**
     * 添加公共参数
     * @param oldRequest
     * @return
     *
     */
    private Request addParam(Request oldRequest) {
        HttpUrl.Builder builder = oldRequest.url()
                .newBuilder()
                .setEncodedQueryParameter("ty", getTy())
                .setEncodedQueryParameter("v", getV())
                .setEncodedQueryParameter("dk", getDk())
                .setEncodedQueryParameter("tn", getTn())
                .setEncodedQueryParameter("userId", getUserId());

        Request newRequest = oldRequest.newBuilder()
                .method(oldRequest.method(), oldRequest.body())
                .url(builder.build())
                .build();
        return newRequest;
    }

    private String ty;
    private String v;
    private String dk;
    private String tn;
    private String userId;

    public void setParams(String ty, String v, String dk, String tn, String userId){
        this.ty = ty;
        this.v = v;
        this.dk = dk;
        this.tn = tn;
        this.userId = userId;
    }

    public String getTy() {
        return ty;
    }

    public String getV() {
        return v;
    }

    public String getDk() {
        return dk;
    }

    public String getTn() {
        return tn;
    }

    public String getUserId() {
        return userId;
    }
}
