package com.bzai.gamesdk.listener;

/**
 * Created by bzai on 2018/4/12.
 * <p>
 * Desc:
 *
 *  对外的SDK Api 账号监听接口
 *
 */

public interface AccountCallBackLister {

    int LOGIN_SUCCESS = 1000; //登录成功
    int LOGIN_FAILURE = 1001; //登录失败
    int LOGIN_CANCEL = 1002;  //登录取消

    int SWITCH_ACCOUNT_SUCCESS = 1003; //切换账号成功
    int SWITCH_ACCOUNT_FAILURE = 1004; //切换账号失败
    int SWITCH_ACCOUNT_CANCEL = 1005; //切换账号取消

    int LOGOUT_SUCCESS = 1006; //注销账号成功
    int LOGOUT_FAILURE = 1007; //注销账号失败
    int LOGOUT_CANCEL = 1008; //注销账号取消

    void onAccountEventCallBack(String jsonStr);

}
