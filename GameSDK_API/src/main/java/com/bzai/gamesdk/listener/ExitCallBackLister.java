package com.bzai.gamesdk.listener;

/**
 * Created by bzai on 2018/4/11.
 * <p>
 * Desc:
 * 对外的SDK Api 退出监听接口
 */

public interface ExitCallBackLister {

    /**
     * 退出框退出成功回调
     */
    void onExitDialogSuccess();

    /**
     * 退出框退出取消回调
     */
    void onExitDialogCancel();

    /**
     * 不存在退出框，需要游戏自己实现退出框
     */
    void onNotExitDialog();
}
