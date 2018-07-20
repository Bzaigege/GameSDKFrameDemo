package com.bzai.gamesdk.common.utils_base.net.base;

import android.content.Context;
import android.text.TextUtils;

import com.bzai.gamesdk.common.utils_base.frame.google.volley.Request;
import com.bzai.gamesdk.common.utils_base.frame.google.volley.Response;
import com.bzai.gamesdk.common.utils_base.frame.google.volley.VolleyError;
import com.bzai.gamesdk.common.utils_base.frame.google.volley.toolbox.StringRequest;


/**
 * Created by bzai on 2018/04/09.
 *
 * 封装Volley StringRequest为网络请求基础基类
 *
 */
public abstract class VolleyRequestWrapper extends StringRequest {

    private VolleySingleton baseVolleySingleton;

    private VolleyRequestWrapper(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
    }

    public VolleyRequestWrapper(Context context, int method, String url, final VolleyResponseListener listener){

        this(method, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                    if(listener != null){
                        listener.onResponseSuccess(response);
                    }
            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if(listener != null){
                    listener.onResponseFailure(error);
                }
            }
        });

        baseVolleySingleton = VolleySingleton.getInstance(context.getApplicationContext());
    }

    /**
     * 添加请求到队列
     */
    protected void sendRequest() {
        addRequestToQueue(this);
    }

    private <T> void addRequestToQueue(Request<T> request) {
        if (request == null)
            return;
        baseVolleySingleton.addToRequestQueue(request);
    }

    /**
     * 取消当前的网络请求
     */
    @Override
    public void cancel() {
        super.cancel();
    }

    /**
     * 取消特定的网络请求
     * @param tag
     */
    protected void cancelByTag(String tag){
        if (TextUtils.isEmpty(tag)){
            return;
        }
        baseVolleySingleton.getRequestQueue().cancelAll(tag);
    }
}

