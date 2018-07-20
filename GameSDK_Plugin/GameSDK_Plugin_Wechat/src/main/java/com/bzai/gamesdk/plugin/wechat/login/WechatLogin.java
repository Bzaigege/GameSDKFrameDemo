package com.bzai.gamesdk.plugin.wechat.login;


/**
 * Created by bzai on 2018/6/21.
 * <p>
 * Desc:
 *
 *  封装微信登录
 */

public class WechatLogin {

    public static String TAG = "WechatPay";

    private volatile static WechatLogin INSTANCE;

    private WechatLogin() {
    }

    public static WechatLogin getInstance() {
        if (INSTANCE == null) {
            synchronized (WechatLogin.class) {
                if (INSTANCE == null) {
                    INSTANCE = new WechatLogin();
                }
            }
        }
        return INSTANCE;
    }
}
