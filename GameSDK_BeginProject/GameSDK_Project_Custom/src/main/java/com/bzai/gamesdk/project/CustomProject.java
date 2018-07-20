package com.bzai.gamesdk.project;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.bzai.gamesdk.common.utils_base.config.ErrCode;
import com.bzai.gamesdk.common.utils_base.config.TypeConfig;
import com.bzai.gamesdk.common.utils_base.interfaces.CallBackListener;
import com.bzai.gamesdk.common.utils_base.parse.project.Project;
import com.bzai.gamesdk.common.utils_base.utils.LogUtils;
import com.bzai.gamesdk.module.account.AccountManager;
import com.bzai.gamesdk.module.account.bean.AccountCallBackBean;
import com.bzai.gamesdk.module.init.InitManager;
import com.bzai.gamesdk.module.purchase.PurchaseManager;

import java.util.HashMap;

/**
 * Created by bzai on 2018/7/10.
 * <p>
 * Desc:
 *
 *    模拟自定义SDK项目
 *
 *    (注意生命周期方法必接，且必须初始化后才调用渠道、功能插件的生命周期方法)
 */

public class CustomProject extends Project{

    private final String TAG = getClass().getSimpleName();


    /**
     * 项目实例化入口
     */
    @Override
    protected synchronized void initProject() {
        LogUtils.d(TAG, getClass().getSimpleName() + " has init");
        super.initProject();
    }


    /******************************************      初始化      ****************************************/

    @Override
    public void init(Activity activity, String gameid, String gamekey, final CallBackListener callBackListener) {
        LogUtils.d(TAG,"init");

        if (activity == null || callBackListener == null) {
            callBackListener.onFailure(ErrCode.PARAMS_ERROR,"activity or callBackListener is null");
            return;
        }

        //设置账号监听
        AccountManager.getInstance().setLoginCallBackLister(projectAccountCallBackListener);

        InitManager.getInstance().init(activity, gameid, gamekey, new CallBackListener() {
            @Override
            public void onSuccess(Object object) {
                callBackListener.onSuccess(null);
            }

            @Override
            public void onFailure(int code, String msg) {
                callBackListener.onFailure(code,msg);
            }
        });
    }


    /******************************************      账号      ****************************************/

    /*** SDKApi层设置回调监听 */
    private CallBackListener ApiAccountCallback;

    @Override
    public void setAccountCallBackLister(CallBackListener callBackLister) {
        ApiAccountCallback = callBackLister;
    }

    /**
     * 监听AccountManager登录、切换账号、绑定、注销的回调信息
     */
    private CallBackListener projectAccountCallBackListener = new CallBackListener<AccountCallBackBean>() {

        @Override
        public void onSuccess(AccountCallBackBean callBackBean) {
            ApiAccountCallback.onSuccess(callBackBean);
        }

        @Override
        public void onFailure(int code, String msg) {
            //不会走到这里来
        }
    };

    private void AccountOnFailCallBack(int event, int code, String msg){

        AccountCallBackBean callBackBean = new AccountCallBackBean();
        callBackBean.setEvent(event);
        callBackBean.setErrorCode(code);
        callBackBean.setMsg(msg);
        ApiAccountCallback.onSuccess(callBackBean);
    }


    @Override
    public void login(Activity activity, HashMap<String, Object> loginParams) {
        LogUtils.d(TAG,"login");

        if (!InitManager.getInstance().getInitState()){
            Toast.makeText(activity,"请先初始化",Toast.LENGTH_SHORT).show();
            return;
        }

        if (activity == null ) {
            AccountOnFailCallBack(TypeConfig.LOGIN,ErrCode.PARAMS_ERROR,"activity is null");
            return;
        }

        AccountManager.getInstance().showLoginView(activity,loginParams);
    }


    @Override
    public void switchAccount(Activity activity) {
        LogUtils.d(TAG,"switchAccount");

        if (!InitManager.getInstance().getInitState()){
            Toast.makeText(activity,"请先初始化",Toast.LENGTH_SHORT).show();
            return;
        }

        if (!AccountManager.getInstance().getLoginState()){
            AccountOnFailCallBack(TypeConfig.SWITCHACCOUNT,ErrCode.NO_LOGIN,"account has not login");
            return;
        }

        if (activity == null ) {
            AccountOnFailCallBack(TypeConfig.LOGIN,ErrCode.PARAMS_ERROR,"activity is null");
            return;
        }

        AccountManager.getInstance().switchAccount(activity);
    }

    @Override
    public void logout(Activity activity) {
        LogUtils.d(TAG,"logout");

        if (!InitManager.getInstance().getInitState()){
            Toast.makeText(activity,"请先初始化",Toast.LENGTH_SHORT).show();
            return;
        }

        if (!AccountManager.getInstance().getLoginState()){
            AccountOnFailCallBack(TypeConfig.LOGOUT,ErrCode.NO_LOGIN,"account has not login");
            return;
        }

        if (activity == null ) {
            AccountOnFailCallBack(TypeConfig.LOGIN,ErrCode.PARAMS_ERROR,"activity is null");
            return;
        }

        AccountManager.getInstance().logout(activity);
    }


    /******************************************      购买      ****************************************/


    @Override
    public void pay(Activity activity, HashMap<String, Object> payParams, CallBackListener callBackListener) {
        LogUtils.d(TAG,"pay");

        if (!InitManager.getInstance().getInitState()){
            Toast.makeText(activity,"请先初始化",Toast.LENGTH_SHORT).show();
            return;
        }

        if (!AccountManager.getInstance().getLoginState()){
            callBackListener.onFailure(ErrCode.NO_LOGIN,"account has not login");
            return;
        }

        if (activity == null || payParams == null || callBackListener == null) {
            callBackListener.onFailure(ErrCode.PARAMS_ERROR,"activity or PayParams or callBackListener is null");
            return;
        }

        PurchaseManager.getInstance().showPayView(activity,payParams,callBackListener);
    }


    /******************************************      退出      ****************************************/


    /**
     * 退出SDK
     */
    @Override
    public void exit(Activity activity, CallBackListener callBackListener) {
        LogUtils.d(TAG,"exit");

        if (activity == null || callBackListener == null) {
            callBackListener.onFailure(ErrCode.PARAMS_ERROR,"activity or callBackListener is null");
            return;
        }

        callBackListener.onFailure(ErrCode.NO_EXIT_DIALOG,"channel not exitDialog");
    }


    /*************************************  生命周期接口(必接) ****************************************/

    @Override
    public void onCreate(Activity activity, Bundle savedInstanceState) {
        LogUtils.d(TAG,"onCreate");

        if (InitManager.getInstance().getInitState()){
            super.onCreate(activity, savedInstanceState);
        }
    }

    @Override
    public void onStart(Activity activity) {
        LogUtils.d(TAG,"onStart");

        if (InitManager.getInstance().getInitState()){
            super.onStart(activity);
        }
    }

    @Override
    public void onResume(Activity activity) {
        LogUtils.d(TAG,"onResume");

        if (InitManager.getInstance().getInitState()){
            super.onResume(activity);
        }
    }

    @Override
    public void onPause(Activity activity) {
        LogUtils.d(TAG,"onPause");

        if (InitManager.getInstance().getInitState()){
            super.onPause(activity);
        }
    }

    @Override
    public void onStop(Activity activity) {
        LogUtils.d(TAG,"onStop");

        if (InitManager.getInstance().getInitState()){
            super.onStop(activity);
        }
    }

    @Override
    public void onDestroy(Activity activity) {
        LogUtils.d(TAG,"onDestroy");

        if (InitManager.getInstance().getInitState()){
            super.onDestroy(activity);
        }
    }

    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        LogUtils.d(TAG,"onActivityResult");

        if (InitManager.getInstance().getInitState()){
            super.onActivityResult(activity, requestCode, resultCode, data);
        }
    }

    @Override
    public void onRequestPermissionsResult(Activity activity, int requestCode, String[] permissions,int[] grantResults) {
        LogUtils.d(TAG,"onRequestPermissionsResult");

        if (InitManager.getInstance().getInitState()){
            super.onRequestPermissionsResult(activity, requestCode, permissions, grantResults);
        }
    }
}
