package com.bzai.gamesdk.common.utils_base.net.request;

import android.text.TextUtils;

import com.bzai.gamesdk.common.utils_base.net.base.VolleyResponseListener;
import com.bzai.gamesdk.common.utils_base.net.impl.BaseRequest;
import com.bzai.gamesdk.common.utils_base.net.impl.BaseRequestCallback;

import java.util.Map;

/**
 * Created by bzai on 2018/04/09.
 */

public class VolleyRequestManager implements IRequestManager {

    private BaseRequest request;
    private String header;
    private String userAgent;

    /**
     * 设置请求头 header
     * @param header
     */
    @Override
    public void setHeader(String header){
        this.header = header;
    }

    /**
     * 设置 userAgent
     * @param userAgent
     */
    @Override
    public void setUserAgent(String userAgent){
        this.userAgent = userAgent;
    }


    /**
     * 注意：BaseRequestCallback 回调的泛型为String
     * @param url
     * @param params
     * @param requestCallback
     */
    @Override
    public void get(String url, Map<String,Object> params, final RequestCallback requestCallback) {

        this.request = GetRequest.create(url, params, new BaseRequestCallback<String>() {

            @Override
            public void onSuccess(String result) {
                requestCallback.onSuccess(result);
            }

            @Override
            public void onFailure(int errCodeType, String msg) {
                requestCallback.onFailure(errCodeType,msg);
            }

        });

        if (!TextUtils.isEmpty(header)){
            this.request.setAuthorization(header);
        }

        if (!TextUtils.isEmpty(userAgent)){
            this.request.setUserAgent(userAgent);
        }

        this.request.sendRequest();

    }

    /**
     * 注意：BaseRequestCallback 回调的泛型为String
     * @param url
     * @param params
     * @param requestCallback
     */
    @Override
    public void post(String url, Map<String,Object> params, final RequestCallback requestCallback) {

        this.request = PostRequest.create(url, params, new BaseRequestCallback<String>() {

            @Override
            public void onSuccess(String result) {
                requestCallback.onSuccess(result);
            }

            @Override
            public void onFailure(int errCodeType, String msg) {
                requestCallback.onFailure(errCodeType,msg);
            }

        });

        if (!TextUtils.isEmpty(header)){
            this.request.setAuthorization(header);
        }

        if (!TextUtils.isEmpty(userAgent)){
            this.request.setUserAgent(userAgent);
        }

        this.request.sendRequest();

    }

    /**
     * 取消网络请求
     */
    @Override
    public void cancel() {
        this.request.cancel();
    }


    /************************************************* 网络请求 ************************************************/

    private static class GetRequest extends BaseRequest {

        public GetRequest(int method, String url, Map<String, Object> bodyMap, VolleyResponseListener listener) {
            super(method, url, bodyMap, listener);
        }

        public static GetRequest create(String url, Map<String, Object> bodyMap, BaseRequestCallback listener){
            return new GetRequest(Method.GET,url,bodyMap,listener);
        }

    }

    private static class PostRequest extends BaseRequest {

        public PostRequest(int method, String url, Map<String, Object> bodyMap, VolleyResponseListener listener) {
            super(method, url, bodyMap, listener);
        }

        public static PostRequest create(String url, Map<String, Object> bodyMap, BaseRequestCallback listener){
            return new PostRequest(Method.POST,url,bodyMap,listener);
        }

    }

    /************************************************* 网络请求 ************************************************/
}
