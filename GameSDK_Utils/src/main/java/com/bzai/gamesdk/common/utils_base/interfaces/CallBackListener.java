package com.bzai.gamesdk.common.utils_base.interfaces;


/**
 * Created by bzai on 2018/4/9.
 * 项目回调基类
 */
public interface CallBackListener<T>  {

    /**
     * 成功回调
     * @param t 详细信息
     */
    void onSuccess(T t);

    /**
     * 失败回调
     *
     * @param code 错误码
     * @param msg 错误详细描述信息
     */
    void onFailure(int code, String msg);

}
