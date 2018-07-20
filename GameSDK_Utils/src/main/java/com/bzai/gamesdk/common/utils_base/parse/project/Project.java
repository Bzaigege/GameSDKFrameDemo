package com.bzai.gamesdk.common.utils_base.parse.project;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.bzai.gamesdk.common.utils_base.interfaces.CallBackListener;
import com.bzai.gamesdk.common.utils_base.parse.plugin.PluginManager;
import com.bzai.gamesdk.common.utils_base.proguard.ProguardInterface;

import java.util.HashMap;

/**
 * Created by bzai on 2018/4/10.
 * <p>
 * Desc:
 *      基础项目工程共有的方法：
 *      初始化、登陆、支付...
 *      生命周期方法..
 */

public abstract class Project implements ProguardInterface {

    /*****************************   Project 加载接口    **********************************/

    public ProjectBeanList.ProjectBean projectBean;

    private boolean hasInited;
    protected synchronized void initProject() {
        if (hasInited) {
            return;
        }
        hasInited = true;
    }

    @Override
    public String toString() {
        return "Project{" + "projectBean=" + projectBean + ", hasInited=" + hasInited + '}';
    }


    /*****************************  顶层 Project 功能接口：初始化、登陆、支付、退出    **********************************/


    /**
     * 初始化
     */
    public void init(Activity activity, String gameid, String gamekey, CallBackListener callBackListener) {

    }

    /**
     * 登录
     */
    public void login(Activity activity, HashMap<String,Object> loginParams) {

    }


    /**
     * 支付
     */
    public void pay(Activity activity, HashMap<String,Object> payParams, CallBackListener callBackListener) {

    }

    /**
     * 切换账号
     */
    public void switchAccount(Activity activity){

    }

    /**
     * 登出
     */
    public void logout(Activity activity) {

    }


    /**
     * 退出
     */
    public void exit(Activity activity, CallBackListener callBackListener) {

    }

    /**
     * 上报数据
     */
    public void reportData(Context context, HashMap<String,Object> dataMap){

    }

    /**
     * 设置SDK账号监听
     */
    public void setAccountCallBackLister(CallBackListener callBackLister){

    }

    /**
     * 显示SDK悬浮窗,将登录、支付等信息回调
     */
    public void showFloatView(Activity activity){}

    /**
     * 关闭SDK悬浮窗
     */
    public void dismissFloatView(Activity activity){}


    /**
     * 拓展接口，处理渠道的定制接口
     */
    public void extendFunction(Activity activity, int functionType, Object object, CallBackListener callBackListener){

    }

    /**
     * 获取渠道ID
     * @return
     */
    public String getChannelID(){
        return null;
    }


    /*******************************  顶层 Project 生命周期接口 (目前实现各插件的生命周期)******************************/

    public void onCreate(Activity activity, Bundle savedInstanceState) {
        PluginManager.getInstance().onCreate(activity, savedInstanceState);
    }

    public void onStart(Activity activity) {
        PluginManager.getInstance().onStart(activity);
    }

    public void onResume(Activity activity) {
        PluginManager.getInstance().onResume(activity);
    }

    public void onPause(Activity activity) {
        PluginManager.getInstance().onPause(activity);
    }

    public void onStop(Activity activity) {
        PluginManager.getInstance().onStop(activity);
    }

    public void onRestart(Activity activity) {
        PluginManager.getInstance().onRestart(activity);
    }

    public void onDestroy(Activity activity) {
        PluginManager.getInstance().onDestroy(activity);
    }

    public void onNewIntent(Activity activity, Intent intent) {
        PluginManager.getInstance().onNewIntent(activity,intent);
    }

    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        PluginManager.getInstance().onActivityResult(activity, requestCode, requestCode, data);
    }

    public void onRequestPermissionsResult(Activity activity, int requestCode, String[] permissions, int[] grantResults) {
        PluginManager.getInstance().onRequestPermissionsResult(activity,requestCode,permissions,grantResults);
    }
}