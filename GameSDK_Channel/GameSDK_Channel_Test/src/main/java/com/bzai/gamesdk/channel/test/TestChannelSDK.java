package com.bzai.gamesdk.channel.test;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.method.PasswordTransformationMethod;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.bzai.gamesdk.common.utils_base.config.TypeConfig;
import com.bzai.gamesdk.common.utils_base.interfaces.CallBackListener;
import com.bzai.gamesdk.common.utils_base.parse.channel.Channel;
import com.bzai.gamesdk.common.utils_base.utils.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by bzai on 2018/7/10.
 * <p>
 * Desc:
 *
 *   测试渠道SDK
 */

public class TestChannelSDK extends Channel {

    private final String TAG = getClass().getSimpleName();

    @Override
    protected void initChannel() {
        LogUtils.d(TAG, getClass().getSimpleName() + " has init");
    }

    @Override
    public String getChannelID() {
        return "1";
    }

    @Override
    public boolean isSupport(int FuncType) {

        switch (FuncType){
            case TypeConfig.FUNC_SWITCHACCOUNT:
                return true;

            case TypeConfig.FUNC_LOGOUT:
                return true;

            case TypeConfig.FUNC_SHOW_FLOATWINDOW:
                return true;

            case TypeConfig.FUNC_DISMISS_FLOATWINDOW:
                return true;

            default:
                return false;
        }
    }

    @Override
    public void init(Context context, HashMap<String, Object> initMap, CallBackListener initCallBackListener) {
        LogUtils.d(TAG,getClass().getSimpleName() + " init");
        initOnSuccess(initCallBackListener);
    }

    @Override
    public void login(Context context, HashMap<String, Object> loginMap, CallBackListener loginCallBackListener) {
        LogUtils.d(TAG,getClass().getSimpleName() + " login");
        showLoginView(context,loginCallBackListener);
    }

    @Override
    public void switchAccount(final Context context, final CallBackListener changeAccountCallBackLister) {

        LogUtils.d(TAG,getClass().getSimpleName() + " switchAccount");
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("是否切换账号?");
        builder.setTitle("切换账号");
        builder.setPositiveButton("切换账号",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int index) {
                        showLoginView(context,changeAccountCallBackLister);
                    }
                });
        builder.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int index) {
                        switchAccountOnCancel("channel switchAccount cancel",changeAccountCallBackLister);
                    }
                });
        builder.create().show();
    }

    @Override
    public void logout(Context context, final CallBackListener logoutCallBackLister) {

        LogUtils.d(TAG,getClass().getSimpleName() + " logout");
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("是否注销账号?");
        builder.setTitle("注销账号");
        builder.setPositiveButton("成功",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int index) {
                        logoutOnSuccess(logoutCallBackLister);
                    }
                });
        builder.setNegativeButton("失败",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int index) {
                        logoutOnFail("channel logout fail",logoutCallBackLister);
                    }
                });
        builder.create().show();

    }

    @Override
    public void pay(Context context, HashMap<String, Object> payMap, final CallBackListener payCallBackListener) {

        LogUtils.d(TAG,getClass().getSimpleName() + " pay");

        String orderID = (String) payMap.get("orderId");
        String productName = (String) payMap.get("productName");
        String productDesc = (String) payMap.get("productDesc");
        String money = String.valueOf(payMap.get("money"));
        String productID = String.valueOf(payMap.get("productID"));
        LogUtils.d(TAG,productID);

        final HashMap<String,Object> paymap = new HashMap<>();
        paymap.put("orderID",orderID);
        paymap.put("productName",productName);
        paymap.put("money",money);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        String message = "充值金额：" + money
                + "\n商品名称：" + productName
                + "\n商品数量：" + "1"
                + "\n资费说明：" + productDesc;
        builder.setMessage(message);
        builder.setTitle("请确认充值信息");
        builder.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int index) {
                        payOnSuccess(payCallBackListener);
                    }
                });
        builder.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int index) {
                        OnCancel(payCallBackListener);
                    }
                });
        builder.create().show();

    }


    private void showLoginView(final Context context, final CallBackListener loginCallBackListener){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("是否登录?");
        builder.setTitle("登录界面");
        builder.setPositiveButton("登录",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int index) {
                        JSONObject json = new JSONObject();
                        try {
                            json.put("sid", "testID");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        loginOnSuccess(json.toString(),loginCallBackListener);
                    }
                });
        builder.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int index) {
                        loginOnFail("channel login fail",loginCallBackListener);
                    }
                });
        builder.create().show();

    }


    @Override
    public void exit(Context context, CallBackListener exitCallBackLister) {
        LogUtils.d(TAG,getClass().getSimpleName() + " exit");
        channelNotExitDialog(exitCallBackLister);
    }


}
