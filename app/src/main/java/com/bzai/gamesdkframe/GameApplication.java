package com.bzai.gamesdkframe;

import android.content.Context;

import com.bzai.gamesdk.SDKApplication;


/**
 * Created by bzai on 2018/4/10.
 * <p>
 * Desc: 游戏的Application 直接继承SDK的application
 */

public class GameApplication extends SDKApplication {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
