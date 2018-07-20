package com.bzai.gamesdk.listener;

/**
 * Created by bzai on 2018/4/10.
 * <p>
 * Desc:
 *
 *  对外的SDK Api 初始化监听接口
 */

public interface InitCallBackLister {

    /**
     * 初始化成功
     */
    void onSuccess();

    /**
     * 初始化失败
     * @param errCode
     * @param msg
     */
    void onFailure(int errCode, String msg);


}
