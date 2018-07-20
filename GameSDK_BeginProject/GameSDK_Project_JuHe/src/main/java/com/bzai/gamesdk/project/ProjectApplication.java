package com.bzai.gamesdk.project;

import android.content.Context;
import com.bzai.gamesdk.channel.application.ChannelApplication;
import com.bzai.gamesdk.module.init.InitManager;


/**
 * Created by bzai on 2018/4/10.
 * <p>
 * Desc: 项目SDK的application
 *
 *     ProjectApplication extends ChannelApplication
 */
public class ProjectApplication extends ChannelApplication {

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
