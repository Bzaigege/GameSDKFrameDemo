package com.bzai.gamesdk.plugin.wechat;

import android.content.Context;

import com.bzai.gamesdk.common.utils_base.interfaces.CallBackListener;
import com.bzai.gamesdk.common.utils_base.parse.plugin.Plugin;
import com.bzai.gamesdk.common.utils_base.utils.LogUtils;
import com.bzai.gamesdk.plugin.wechat.pay.WechatPay;

import java.util.Map;

/**
 * Created by bzai on 2018/6/20.
 * <p>
 * Desc:
 *
 *  微信功能插件,方便后后续添加登录接口，支付(H5支付)接口，统计接口
 */

public class WechatPlugin extends Plugin {

    private String TAG = "WechatPlugin";

    @Override
    protected synchronized void initPlugin() {
        super.initPlugin();
        LogUtils.d(TAG,"init " + getClass().getSimpleName());
    }

    /**
     * 调用微信支付接口
     */
    public void wechatPay(Context context, Map<String,Object> payMap, CallBackListener callBackListener){
        WechatPay.getInstance().pay(context,payMap,callBackListener);
    }


    /**
     * 调用微信登录接口
     */
    public void wechatLogin(Context context, Map<String,Object> LoginMap, CallBackListener callBackListener){

    }


    /**
     * 调用微信分享接口
     */
    public void wechatShare(Context context, Map<String,Object> ShareMap, CallBackListener callBackListener){

    }

    /**
     * 根据当前的生命周期
     * @param context
     */
    @Override
    public void onResume(Context context) {
        WechatPay.getInstance().onResume(context);
    }
}
