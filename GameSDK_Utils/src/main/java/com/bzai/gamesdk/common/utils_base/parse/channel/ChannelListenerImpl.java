package com.bzai.gamesdk.common.utils_base.parse.channel;


import com.bzai.gamesdk.common.utils_base.config.ErrCode;
import com.bzai.gamesdk.common.utils_base.config.TypeConfig;
import com.bzai.gamesdk.common.utils_base.interfaces.CallBackListener;

import java.util.HashMap;

/**
 * Created by bzai on 2018/4/13.
 * <p>
 * Desc:
 *
 *  统一封装渠道回调信息,Channel 需继承
 */

public class ChannelListenerImpl {

    private HashMap<String,Object> loginAuthData;

    /**
     * 初始化成功
     */
    public void initOnSuccess(CallBackListener callBackListener){
        if (callBackListener != null){
            callBackListener.onSuccess(null);
        }
    }


    /**
     * 登录成功
     */
    public void loginOnSuccess(String oathUrl, CallBackListener callBackListener){

        if (loginAuthData == null){
            loginAuthData = new HashMap<>();
        }

        loginAuthData.put(Channel.PARAMS_OAUTH_URL,oathUrl); //授权的参数信息
        loginAuthData.put(Channel.PARAMS_OAUTH_TYPE, TypeConfig.LOGIN); //授权的事件类型

        if (callBackListener != null){
            callBackListener.onSuccess(loginAuthData);
        }
    }


    /**
     * 登录失败
     */
    public void loginOnFail(String errorMessage, CallBackListener callBackListener){

        if (callBackListener != null){
            callBackListener.onFailure(ErrCode.CHANNEL_LOGIN_FAIL,errorMessage);
        }

    }

    /**
     * 登录取消
     */
    public void loginOnCancel(String errorMessage, CallBackListener callBackListener){

        if (callBackListener != null){
            callBackListener.onFailure(ErrCode.CHANNEL_LOGIN_CANCEL,errorMessage);
        }

    }


    /**
     * 切换账号成功
     */
    public void switchAccountOnSuccess(String oathUrl, CallBackListener callBackListener){

        if (loginAuthData == null){
            loginAuthData = new HashMap<>();
        }

        loginAuthData.put(Channel.PARAMS_OAUTH_URL,oathUrl); //授权的参数信息
        loginAuthData.put(Channel.PARAMS_OAUTH_TYPE, TypeConfig.SWITCHACCOUNT); //授权的事件类型

        if (callBackListener != null){
            callBackListener.onSuccess(loginAuthData);
        }
    }

    /**
     * 切换账号成功
     */
    public void switchAccountOnFail(String errorMessage, CallBackListener callBackListener){

        if (callBackListener != null){
            callBackListener.onFailure(ErrCode.CHANNEL_SWITCH_ACCOUNT_FAIL,errorMessage);
        }

    }


    /**
     * 切换账号成功
     */
    public void switchAccountOnCancel(String errorMessage, CallBackListener callBackListener){

        if (callBackListener != null){
            callBackListener.onFailure(ErrCode.CHANNEL_SWITCH_ACCOUNT_CANCEL,errorMessage);
        }

    }

    /**
     * 注销成功
     */
    public void logoutOnSuccess(CallBackListener callBackListener){

        if (loginAuthData == null){
            loginAuthData = new HashMap<>();
        }

        loginAuthData.put(Channel.PARAMS_OAUTH_URL,""); //授权的参数信息
        loginAuthData.put(Channel.PARAMS_OAUTH_TYPE, TypeConfig.LOGOUT); //授权的事件类型

        if (callBackListener != null){
            callBackListener.onSuccess(loginAuthData);
        }
    }

    /**
     * 注销失败
     */
    public void logoutOnFail(String errorMessage, CallBackListener callBackListener){

        if (callBackListener != null){
            callBackListener.onFailure(ErrCode.CHANNEL_LOGOUT_FAIL,errorMessage);
        }
    }

    /**
     * 注销取消
     */
    public void logoutOnCancel(String errorMessage, CallBackListener callBackListener){

        if (callBackListener != null){
            callBackListener.onFailure(ErrCode.CHANNEL_LOGOUT_CANCEL,errorMessage);
        }
    }


    /**
     * 支付成功
     * @param callBackListener
     */
    public void payOnSuccess(CallBackListener callBackListener){

        if (callBackListener != null){
            callBackListener.onSuccess(null);
        }
    }

    /**
     * 当渠道不能正确返回支付结果时，返回该字段
     * @param callBackListener
     */
    public void payOnComplete(CallBackListener callBackListener){

        if (callBackListener != null){
            callBackListener.onFailure(ErrCode.NO_PAY_RESULT,"pay complete");
        }
    }

    /**
     * 统一渠道不存在退出框
     */
    public void channelNotExitDialog(CallBackListener callBackListener){

        if (callBackListener != null){
            callBackListener.onFailure(ErrCode.NO_EXIT_DIALOG,"channel not exitDialog");
        }

    }


    /**
     * 统一失败回调
     */
    public void OnFailure(String errorMessage, CallBackListener callBackListener){

        if (callBackListener != null){
            callBackListener.onFailure(ErrCode.FAILURE,errorMessage);
        }

    }

    /**
     * 统一取消回调
     */
    public void  OnCancel(CallBackListener callBackListener){

        if (callBackListener != null){
            callBackListener.onFailure(ErrCode.CANCEL,"cancel");
        }
    }

}
