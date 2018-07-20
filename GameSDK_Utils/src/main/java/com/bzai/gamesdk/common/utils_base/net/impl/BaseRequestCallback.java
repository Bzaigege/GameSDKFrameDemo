package com.bzai.gamesdk.common.utils_base.net.impl;

import android.text.TextUtils;

import com.bzai.gamesdk.common.utils_base.config.ErrCode;
import com.bzai.gamesdk.common.utils_base.frame.google.volley.AuthFailureError;
import com.bzai.gamesdk.common.utils_base.frame.google.volley.NetworkError;
import com.bzai.gamesdk.common.utils_base.frame.google.volley.NoConnectionError;
import com.bzai.gamesdk.common.utils_base.frame.google.volley.ParseError;
import com.bzai.gamesdk.common.utils_base.frame.google.volley.RedirectError;
import com.bzai.gamesdk.common.utils_base.frame.google.volley.ServerError;
import com.bzai.gamesdk.common.utils_base.frame.google.volley.TimeoutError;
import com.bzai.gamesdk.common.utils_base.frame.google.volley.VolleyError;
import com.bzai.gamesdk.common.utils_base.net.base.VolleyResponseListener;

/**
 * Created by bzai on 2018/04/09.
 *
 * 处理请求返回数据分类，通过泛型将结果数据转换成对应实体回传
 * 
 * 
 */
public abstract class BaseRequestCallback<T> implements VolleyResponseListener{

    @Override
    public void onResponseSuccess(String response) {

        if (TextUtils.isEmpty(response)){
            onFailure(ErrCode.NET_DATA_NULL, "net data null");//网络请求成功,但是数据为空错误。
            return;
        }

        onSuccess((T) response);

    }

    @Override
    public void onResponseFailure(VolleyError error) {
        //处理下回调信息
//        onFailure(ErrCode.NET_ERROR, error.toString());
        Class errorClass = error.getClass();
        if (errorClass.equals(AuthFailureError.class)){//网络认证失败
            onFailure(ErrCode.NET_ERROR, "Network AuthFailureError");

        }else if (errorClass.equals(NetworkError.class)){ //网络错误
            onFailure(ErrCode.NET_ERROR, "NetworkError");

        }else if (errorClass.equals(NoConnectionError.class)){ //无网络连接
            onFailure(ErrCode.NET_ERROR, "Network NoConnectionError");

        }else if (errorClass.equals(ParseError.class)){ //服务器响应无法解析
            onFailure(ErrCode.NET_ERROR, "Network ParseError");

        }else if (errorClass.equals(RedirectError.class)){ //已存在重定向
            onFailure(ErrCode.NET_ERROR, "Network RedirectError");

        }else if (errorClass.equals(ServerError.class)){ //服务器错误响应
            onFailure(ErrCode.NET_ERROR, "Network ServerError");

        }else if (errorClass.equals(TimeoutError.class)){ //连接超时
            onFailure(ErrCode.NET_ERROR, "Network TimeoutError");
        }
    }


    /**
     * 成功后回调方法
     *
     * @param t
     */
    public abstract void onSuccess(T t);

    /**
     * 失败后回调方法
     *
     * @param errCode
     * @param msg
     */
    public abstract void onFailure(int errCode, String msg);

}
