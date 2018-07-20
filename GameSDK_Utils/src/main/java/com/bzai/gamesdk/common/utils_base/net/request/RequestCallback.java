package com.bzai.gamesdk.common.utils_base.net.request;


/**
 * Created by bzai on 2018/04/09.
 */

public interface RequestCallback {

    /**
     * 网络请求回调成功
     * @param object
     */
    void onSuccess(Object object);


    /**
     * 网络请求回调失败
     * @param errCode
     * @param message
     */
    void onFailure(int errCode, String message);

}
