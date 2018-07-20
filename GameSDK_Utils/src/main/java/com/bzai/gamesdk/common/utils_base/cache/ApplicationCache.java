package com.bzai.gamesdk.common.utils_base.cache;

import android.app.Application;
import android.content.Context;

/**
 * Created by bzai on 2018/7/16.
 * <p>
 * Desc:
 */
public class ApplicationCache {

    private Application mAppContext;
    public Context getApplication() {
        return mAppContext;
    }

    /**
     * 缓存全局的ApplicationContext
     * @return
     */
    public Context getApplicationContext(){
        return mAppContext.getApplicationContext();
    }

    /********************* 同步锁双重检测机制实现单例模式（懒加载）********************/

    private volatile static ApplicationCache sCache;
    private ApplicationCache(Application appContext) {
        mAppContext = appContext;
    }

    public static ApplicationCache getInstance() {
        if (sCache == null) {
            throw new RuntimeException("get(Context) never called");
        }
        return sCache;
    }

    public static ApplicationCache init(Application cxt) {
        if (sCache == null) {
            synchronized (ApplicationCache.class) {
                if (sCache == null) {
                    sCache = new ApplicationCache(cxt);
                }
            }
        }
        return sCache;
    }
}
