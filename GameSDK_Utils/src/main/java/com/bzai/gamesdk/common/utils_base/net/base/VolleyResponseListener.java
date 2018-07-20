package com.bzai.gamesdk.common.utils_base.net.base;

import com.bzai.gamesdk.common.utils_base.frame.google.volley.VolleyError;


/**
 * Created by bzai on 2018/04/09.
 *
 * 网络请求回调，封装Volley请求回调
 */
public interface VolleyResponseListener{

    /**
     * 网络请求成功
     * @param response
     */
    void onResponseSuccess(String response);

    /**
     * 网络请求失败
     *
     * @param error
     */
    void onResponseFailure(VolleyError error);

}
