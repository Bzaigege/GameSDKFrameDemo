package com.bzai.gamesdk.plugin.alipay;

import android.content.Context;


import com.bzai.gamesdk.common.utils_base.interfaces.CallBackListener;
import com.bzai.gamesdk.common.utils_base.parse.plugin.Plugin;
import com.bzai.gamesdk.common.utils_base.utils.LogUtils;

import java.util.Map;

/**
 * Created by bzai on 2018/6/21.
 * <p>
 * Desc:
 *
 *  支付宝功能插件,方便后后续添加登录接口，支付(H5支付)接口，统计接口
 */

public class AlipayPlugin extends Plugin {

    private String TAG = "AlipayPlugin";

    @Override
    protected synchronized void initPlugin() {
        super.initPlugin();
        LogUtils.d(TAG,"init " + getClass().getSimpleName());
    }

    /**
     * 调用支付宝支付app接口
     */
    public void alipay(Context context, Map<String,Object> payMap, CallBackListener callBackListener){
//        AlipayPay.getInstance().pay(context,payMap,callBackListener);
    }

}
