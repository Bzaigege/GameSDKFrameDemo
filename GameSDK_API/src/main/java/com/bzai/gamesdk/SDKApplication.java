package com.bzai.gamesdk;
import android.content.Context;

import com.bzai.gamesdk.project.ProjectApplication;

/**
 * Created by bzai on 2018/4/10.
 * <p>
 * Desc: 项目SDK的application
 */

public class SDKApplication extends ProjectApplication {

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

}
