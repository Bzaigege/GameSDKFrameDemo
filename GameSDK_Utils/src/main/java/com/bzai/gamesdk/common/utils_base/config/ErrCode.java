package com.bzai.gamesdk.common.utils_base.config;

/**
 * Created by bzai on 2018/4/9.
 *
 * 全局错误码类
 *
 */

public class ErrCode {

    //--------------------- 公共(1000~1200) -------------------------

    public static final int SUCCESS = 1000; //成功
    public static final int FAILURE = 1001; //失败
    public static final int CANCEL = 1002;  //取消
    public static final int UNKONW = 1003;  //未知错误
    public static final int PARAMS_ERROR = 1004;  //参数错误

    public static final int NO_LOGIN = 1005;  //没有登录
    public static final int NO_PAY_RESULT = 1006;  //渠道没有正确返回支付结果
    public static final int NO_EXIT_DIALOG = 1007;  //渠道没有退出框
    public static final int CHANNEL_LOGIN_CLOSE = 1008;  //后台关闭当前渠道登录
    public static final int CHANNEL_PAY_CLOSE = 1009;  //后台关闭当前渠道支付


    //---------------------网络(1200 ~ 1500 )-------------------------
    public static final int NET_DISCONNET = 1201; //无网络连接
    public static final int NET_ERROR = 1202; //访问网络异常
    public static final int NET_TIME_OUT = 1203; //超时

    public static final int NET_DATA_NULL = 1204; ////网络请求成功,但是数据为空
    public static final int NET_DATA_ERROR = 1205; ////网络请求成功,但是数据类型错误。
    public static final int NET_DATA_EXCEPTION = 1206; //网络请求成功,但是数据解析异常。


    //--------------------- 针对渠道的错误码 -------------------------
    public static final int CHANNEL_LOGIN_FAIL = 1501; //登录失败
    public static final int CHANNEL_LOGIN_CANCEL = 1502; //登录取消

    public static final int CHANNEL_SWITCH_ACCOUNT_FAIL = 1503; //切换账号失败
    public static final int CHANNEL_SWITCH_ACCOUNT_CANCEL = 1504; //切换账号取消

    public static final int CHANNEL_LOGOUT_FAIL = 1505; //注销失败
    public static final int CHANNEL_LOGOUT_CANCEL = 1506; //注销取消









}
