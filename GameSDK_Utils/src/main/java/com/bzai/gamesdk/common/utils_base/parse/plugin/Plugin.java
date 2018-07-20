package com.bzai.gamesdk.common.utils_base.parse.plugin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.bzai.gamesdk.common.utils_base.interfaces.LifeCycleInterface;
import com.bzai.gamesdk.common.utils_base.proguard.ProguardInterface;


/**
 * Created by bzai on 2018/4/9.
 * 基础功能插件基类
 */

public class Plugin implements LifeCycleInterface, ProguardInterface {

    private static final String TAG = "Plugin";

    public PluginBeanList.PluginBean pluginBean;

    private boolean hasInited;

    protected synchronized void initPlugin() {
        if (hasInited) {
            return;
        }
        hasInited = true;
    }

    @Override
    public String toString() {
        return "Plugin{" + "pluginMessage=" + pluginBean + ", hasInited=" + hasInited + '}';
    }

    /****************************************生命周期方法*********************************************/


    public void onCreate(Context context, Bundle savedInstanceState) {
    }

    public void onStart(Context context) {
    }

    public void onResume(Context context) {
    }

    public void onPause(Context context) {
    }

    public void onStop(Context context) {
    }

    public void onRestart(Context context) {
    }

    public void onDestroy(Context context) {
    }

    public void onNewIntent(Context context, Intent intent){

    }

    public void onActivityResult(Context context, int requestCode, int resultCode, Intent data) {
    }

    public void onRequestPermissionsResult(Context context, int requestCode, String[] permissions, int[] grantResults) {
    }
}
