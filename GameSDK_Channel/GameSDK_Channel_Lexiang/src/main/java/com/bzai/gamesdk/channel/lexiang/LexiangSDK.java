package com.bzai.gamesdk.channel.lexiang;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.bzai.gamesdk.common.utils_base.config.TypeConfig;
import com.bzai.gamesdk.common.utils_base.interfaces.CallBackListener;
import com.bzai.gamesdk.common.utils_base.parse.channel.Channel;
import com.bzai.gamesdk.common.utils_base.utils.LogUtils;
import com.youxun.sdk.app.YouxunProxy;
import com.youxun.sdk.app.YouxunXF;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * @author bzai
 * @data 2018/9/11
 * <p>
 * Desc:乐享渠道SDK
 */
public class LexiangSDK extends Channel{

    private final String TAG = getClass().getSimpleName();

    private String game = "tmj";
    private String key = "4444b360a2c5b8d123f527a09c1f9f29";

    private CallBackListener channelAccountCallBackListener;
    private CallBackListener purchaseCallBackListener;

    @Override
    protected void initChannel() {
        LogUtils.d(TAG, getClass().getSimpleName() + " has init");
    }

    @Override
    public String getChannelID() {
        return null;
    }

    @Override
    public boolean isSupport(int FuncType) {

        switch (FuncType){

            case TypeConfig.FUNC_SWITCHACCOUNT:
                return false;

            case TypeConfig.FUNC_LOGOUT:
                return true;

            case TypeConfig.FUNC_SHOW_FLOATWINDOW:
                return false;

            case TypeConfig.FUNC_DISMISS_FLOATWINDOW:
                return false;

            default:
                return false;
        }
    }

    @Override
    public void init(Context context, HashMap<String, Object> initMap, CallBackListener initCallBackListener) {
        LogUtils.d(TAG,getClass().getSimpleName() + " init");

        //乐享SDK初始化
        YouxunProxy.init(game,key);
        initOnSuccess(initCallBackListener);
    }

    @Override
    public void login(Context context, HashMap<String, Object> loginMap, CallBackListener loginCallBackListener) {
        LogUtils.d(TAG,getClass().getSimpleName() + " login");

        channelAccountCallBackListener = loginCallBackListener;
        YouxunProxy.startLogin((Activity) context);
    }

    @Override
    public void switchAccount(Context context, CallBackListener changeAccountCallBackLister) {
        LogUtils.d(TAG,getClass().getSimpleName() + " switchAccount");
    }

    @Override
    public void logout(Context context, CallBackListener logoutCallBackLister) {
        LogUtils.d(TAG,getClass().getSimpleName() + " logout");

        YouxunProxy.exitLogin((Activity) context);

        YouxunXF.onDestroy();//销毁悬浮图标
        logoutOnSuccess(channelAccountCallBackListener);
    }

    @Override
    public void pay(Context context, HashMap<String, Object> payMap, CallBackListener payCallBackListener) {
        LogUtils.d(TAG,getClass().getSimpleName() + " pay");

        String productName = (String) payMap.get("productName");
        String orderId = (String) payMap.get("orderId");
        float price = convertToFloat(payMap.get("money"),0) / 100;
        String money = convertToString(price,"");
        String serverID = (String) payMap.get("serverID");

        purchaseCallBackListener = payCallBackListener;
        YouxunProxy.startPay((Activity) context, productName, money, orderId, serverID);
    }

    @Override
    public void exit(Context context, CallBackListener exitCallBackLister) {
        LogUtils.d(TAG,getClass().getSimpleName() + " exit");
        channelNotExitDialog(exitCallBackLister);
    }

    @Override
    public void onActivityResult(Context context, int requestCode, int resultCode, Intent data) {

        if (requestCode == YouxunProxy.REQUEST_CODE_LOGIN && resultCode == YouxunProxy.RESULT_CODE_LOGIN){

            //登录
            loginCall(context, data);

        }else 	if (requestCode == YouxunProxy.REQUEST_CODE_PAY && resultCode == YouxunProxy.RESULT_CODE_PAY) {

            LogUtils.d(TAG,data.getStringExtra("data"));

            payOnComplete(purchaseCallBackListener);

        }else if (requestCode == YouxunXF.REQUEST_CODE_SWITCH_ACCOUNT && resultCode == YouxunXF.RESULT_CODE_SWITCH_ACCOUNT){

            //切换账号
            YouxunXF.onDestroy();//销毁悬浮图标
            login(context,null,channelAccountCallBackListener); //启动登录
        }

    }

    @Override
    public void onDestroy(Context context) {
        YouxunXF.onDestroy();//销毁悬浮图标
    }

    private void loginCall(Context context, Intent data){

        if (data.getStringExtra("data").equals("success")){

            //登入成功
            String userId = data.getStringExtra("userid");
            String msg = data.getStringExtra("msg");
            String time = data.getStringExtra("time");
            String sign = data.getStringExtra("sign");
            LogUtils.debug_d(TAG,"userid=" + userId + "\n" +
                    "msg=" + msg + "\n" + "time=" + time + "\n" + "sign=" + sign + "\n");

            //检测版本
            YouxunProxy.updateDialog(context, data);

            //提示用户账号信息
            YouxunXF.hintUserInfo((Activity)context);

            //创建悬浮图标
            YouxunXF.onCreate((Activity)context,0.4f);

            //将数据格式返回到上一层
            JSONObject json = new JSONObject();
            try {
                json.put("sid", userId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            loginOnSuccess(json.toString(),channelAccountCallBackListener);

        }else {

            //登录失败
            loginOnFail("channel login fail",channelAccountCallBackListener);
        }
    }


    /**
     * 转化为float
     * @param value 传入对象
     * @param defaultValue 发生异常时，返回默认值
     * @return
     */
    public float convertToFloat(Object value, float defaultValue){

        if (value == null || "".equals(value.toString().trim())){
            return defaultValue;
        }

        try {
            return Float.valueOf(value.toString());
        }catch (Exception e){

            try {
                return Float.valueOf(String.valueOf(value));
            }catch (Exception e1) {

                try {
                    return Long.valueOf(value.toString()).floatValue();
                }catch (Exception e2){
                    return defaultValue;
                }
            }
        }
    }

    /**
     * 转化为float
     * @param value 传入对象
     * @param defaultValue 发生异常时，返回默认值
     * @return
     */
    public String convertToString(Object value, String defaultValue){

        if (value == null || "".equals(value.toString().trim())){
            return defaultValue;
        }

        //普通数据类型先转化
        try {
            return String.valueOf(value);
        }catch (Exception e){

            try {
                return value.toString();
            }catch (Exception e1) {
                return defaultValue;
            }
        }
    }
}
