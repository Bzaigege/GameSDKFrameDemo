package com.bzai.gamesdk.listener;

/**
 * Created by bzai on 2018/4/13.
 * <p>
 * Desc:
 *
 * 支付回调
 *
 */

public interface PurchaseCallBackListener {

    /**
     * 返回订单成功信息,比支付结果早
     * @param orderId
     */
    void onOrderId(String orderId);

    /**
     * 支付成功
     */
    void onSuccess();

    /**
     * 支付失败回调
     * @param errCode
     * @param msg 失败信息
     *
     */
    void onFailure(int errCode, String msg);

    /**
     * 支付取消
     */
    void onCancel();


    /**
     * 支付完成，当渠道没有正确支付回调时，会返回该结果
     */
    void onComplete();
}
