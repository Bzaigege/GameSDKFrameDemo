package com.bzai.gamesdk.channel.application;

import android.app.Application;
import android.content.Context;

/**
 * Created by bzai on 2018/4/11.
 * <p>
 * Desc:
 *
 *  预留用于继承渠道的Application
 */

public class ChannelApplication extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }


}
