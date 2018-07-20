package com.bzai.gamesdk.invoke.plugin;

import android.content.Context;


import com.bzai.gamesdk.common.utils_base.interfaces.CallBackListener;
import com.bzai.gamesdk.common.utils_base.parse.plugin.Plugin;
import com.bzai.gamesdk.common.utils_base.parse.plugin.PluginManager;
import com.bzai.gamesdk.common.utils_base.parse.plugin.PluginReflectApi;

import java.util.Map;

/**
 * Created by bzai on 2018/6/21.
 * <p>
 * Desc:
 *
 *  对接支付宝功能插件的api,反射调用
 */

public class AlipayPluginApi extends PluginReflectApi {

    private String TAG = "AlipayPluginApi";

    private Plugin alipayPlugin;

    private volatile static AlipayPluginApi INSTANCE;

    private AlipayPluginApi() {
        alipayPlugin = PluginManager.getInstance().getPlugin("plugin_alipay");
    }

    public static AlipayPluginApi getInstance() {
        if (INSTANCE == null) {
            synchronized (WechatPluginApi.class) {
                if (INSTANCE == null) {
                    INSTANCE = new AlipayPluginApi();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 调用支付宝app支付
     */
    public void pay(Context context, Map<String,Object> map, CallBackListener callBackListener){

        if (alipayPlugin != null){
            invoke(alipayPlugin,"alipay",new Class<?>[]{Context.class, Map.class, CallBackListener.class},
                    new Object[]{context, map, callBackListener});
        }

    }

}
