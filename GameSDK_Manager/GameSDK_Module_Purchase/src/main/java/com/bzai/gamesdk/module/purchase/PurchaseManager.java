package com.bzai.gamesdk.module.purchase;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;


import com.bzai.gamesdk.common.utils_base.config.ErrCode;
import com.bzai.gamesdk.common.utils_base.interfaces.CallBackListener;
import com.bzai.gamesdk.common.utils_base.utils.LogUtils;

import java.util.HashMap;

/**
 * Created by bzai on 2018/4/11.
 * <p>
 * Desc:
 *
 *  购买管理类，管理SDK的各个购买功能接口：创建订单、三方支付、运营商支付、渠道支付、补单逻辑、包月、订阅等。
 *
 *  注意可能还会有各个复杂的支付逻辑: 可能会先短代支付、然后渠道支付、三方支付，还有后台切换支付开关等。
 *
 *  后续项目待定
 */

public class PurchaseManager {

    public static final String TAG = "PurchaseManager";

    private volatile static PurchaseManager INSTANCE;

    private PurchaseManager() {
    }

    public static PurchaseManager getInstance() {
        if (INSTANCE == null) {
            synchronized (PurchaseManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new PurchaseManager();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 创建订单,具体项目具体实现
     */
    public void createOrderId(Activity activity, HashMap<String, Object> payParams , final CallBackListener callBackListener){

        LogUtils.debug_d(TAG,"payParams = " + payParams.toString());
        String orderID = "DD1441";
        callBackListener.onSuccess(orderID);

    }

    /**
     * 显示支付界面
     */
    public void showPayView(Activity activity, HashMap<String, Object> payParams, final CallBackListener callBackListener){
        LogUtils.debug_d(TAG,"payParams = " + payParams.toString());

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        String message = "充值金额：" + "2"
                + "\n商品名称：" + "大饼"
                + "\n商品数量：" + "1"
                + "\n资费说明：" + "2元";
        builder.setMessage(message);
        builder.setTitle("请确认充值信息");
        builder.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int index) {
                        //支付结果回调到这里来
                        PurchaseResult purchaseResult = new PurchaseResult(PurchaseResult.PurchaseState,null);
                        callBackListener.onSuccess(purchaseResult);
                    }
                });
        builder.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int index) {
                        callBackListener.onFailure(ErrCode.FAILURE,"pay fail");
                    }
                });
        builder.create().show();

    }
}
