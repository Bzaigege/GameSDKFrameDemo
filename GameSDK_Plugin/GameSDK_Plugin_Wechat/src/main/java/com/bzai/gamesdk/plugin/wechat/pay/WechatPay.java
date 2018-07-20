package com.bzai.gamesdk.plugin.wechat.pay;

import android.content.Context;
import android.text.TextUtils;

import com.bzai.gamesdk.common.utils_base.interfaces.CallBackListener;
import com.bzai.gamesdk.common.utils_base.utils.LogUtils;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;


import java.util.Map;

/**
 * Created by bzai on 2018/6/21.
 * <p>
 * Desc:
 *
 *   封装微信支付
 */

public class WechatPay {

    public static String TAG = "WechatPay";

    private volatile static WechatPay INSTANCE;

    private WechatPay() {
    }

    public static WechatPay getInstance() {
        if (INSTANCE == null) {
            synchronized (WechatPay.class) {
                if (INSTANCE == null) {
                    INSTANCE = new WechatPay();
                }
            }
        }
        return INSTANCE;
    }


    /**
     * 微信app支付
     */
    public void pay(Context context, Map<String,Object> payMap, CallBackListener callBackListener){

    }


    /**
     * 处理微信没有回调的问题
     * @param context
     */
    public void onResume(Context context) {
        LogUtils.debug_d(TAG,"onResume");

    }

}
