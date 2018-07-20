package com.bzai.gamesdk.common.utils_base.config;

/**
 * Created by bzai on 2018/7/12.
 * <p>
 * Desc:
 *
 *    定义基础的事件配置
 */
public class TypeConfig {

    /**************************** 事件类型 *****************************/

    //账号事件配置  (200 ~ 219) *************
    public static final int LOGIN = 100;  //登录
    public static final int SWITCHACCOUNT = 101;  //切换
    public static final int BIND = 102;  //绑定
    public static final int LOGOUT = 103;  //注销


    //购买事件配置(220~239)
    public static final int PAY = 150;  // 支付
    public static final int SUBS = 151;  // 订阅


    //是否支持该功能接口类型
    public static final int FUNC_SWITCHACCOUNT = 250;  //切换
    public static final int FUNC_LOGOUT = 251;  //注销
    public static final int FUNC_SHOW_FLOATWINDOW = 252;  //显示浮窗
    public static final int FUNC_DISMISS_FLOATWINDOW = 253;  //隐藏浮窗


    /**************************** 事件方式配置 *****************************/


    //登录方式配置(1~30)
    public static final int LOGIN_AUTH = 1;  //三方授权登录(渠道登录)
    public static final int LOGIN_ACCOUNT = 2;  //账号登录
    public static final int LOGIN_PHONE = 3;  //手机登录
    public static final int LOGIN_MAIL = 4;  //邮箱登录
    public static final int LOGIN_TOKEN = 5;  //TOKEN登录
    public static final int LOGIN_QUICK = 6;  //快速登录(随机账号登录/token登录)
    public static final int LOGIN_GOOGLE = 7;  //GOOGLE授权登录
    public static final int LOGIN_FACEBOOK = 8;  //facebook授权登录
    public static final int LOGIN_LINE = 9;    //授权line登录
    public static final int LOGIN_WECHAT = 10;  //微信授权登录
    public static final int LOGIN_QQ = 11;      //QQ授权登录


    //支付方式配置(31 ~60)
    public static final int PAY_AUTH = 31;  //三方支付(渠道支付)
    public static final int PAY_WECHAT = 32;  //微信支付
    public static final int PAY_ALIPAY = 33;  //支付宝支付
    public static final int PAY_UNIONPAY = 34;  //银联支付
    public static final int PAY_GOOGLE = 35;  //google支付
    public static final int PAY_PLATFORM = 36;  //自有平台币支付
    public static final int PAY_REDEMPTION_CODE = 37;  //兑换码支付


    //分享方式配置(61~90)
    public static final int SHARE_WECHAT = 61;  //微信分享
    public static final int SHARE_QQ = 62;  //微信分享
    public static final int SHARE_SINA = 63;  //新浪微博分享
    public static final int SHARE_FACEBOOK = 64;  //facebook分享
    public static final int SHARE_GOOGLE = 65;  //google分享
    public static final int SHARE_LINE = 66;  //line分享

}
