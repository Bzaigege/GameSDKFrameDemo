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
 *   对接微信功能插件的api,反射调用
 */

public class WechatPluginApi extends PluginReflectApi {

    private String TAG = "WechatPluginApi";

    private Plugin wechatPlugin;

    private volatile static WechatPluginApi INSTANCE;

    private WechatPluginApi() {
        wechatPlugin = PluginManager.getInstance().getPlugin("plugin_wechat");
    }

    public static WechatPluginApi getInstance() {
        if (INSTANCE == null) {
            synchronized (WechatPluginApi.class) {
                if (INSTANCE == null) {
                    INSTANCE = new WechatPluginApi();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 调用微信app支付
     */
    public void pay(Context context, Map<String,Object> map, CallBackListener callBackListener){

        if (wechatPlugin != null){
            invoke(wechatPlugin,"wechatPay",new Class<?>[]{Context.class, Map.class, CallBackListener.class},
                    new Object[]{context, map, callBackListener});
        }
    }

}
