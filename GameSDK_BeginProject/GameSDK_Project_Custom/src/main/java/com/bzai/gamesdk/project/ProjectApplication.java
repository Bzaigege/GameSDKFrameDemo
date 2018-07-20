package com.bzai.gamesdk.project;

import android.app.Application;
import android.content.Context;

import com.bzai.gamesdk.module.init.InitManager;

/**
 * Created by bzai on 2018/4/10.
 * <p>
 * Desc: 项目SDK的application
 */
public class ProjectApplication extends Application {

    @Override
    protected void attachBaseContext(Context context) {
        //必须先加载项目配置文件，找到项目的入口。
        InitManager.getInstance().initApplication(this,context,true);
        super.attachBaseContext(context);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }


}
