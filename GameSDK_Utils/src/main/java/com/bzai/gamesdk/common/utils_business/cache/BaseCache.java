package com.bzai.gamesdk.common.utils_business.cache;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;


import com.bzai.gamesdk.common.utils_base.frame.walle.WalleChannelReader;
import com.bzai.gamesdk.common.utils_base.frame.walle.payload_reader.ChannelInfo;
import com.bzai.gamesdk.common.utils_business.config.KeyConfig;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by bzai on 2018/4/9.
 *
 * 全局缓存数据类
 *
 */

public class BaseCache {

    private static final String TAG = "BaseCache";

    private Application mAppContext;
    public Context getApplication() {
        return mAppContext;
    }

    /********************* 同步锁双重检测机制实现单例模式（懒加载）********************/

    private volatile static BaseCache sCache;
    private BaseCache(Application appContext) {
        mAppContext = appContext;
    }

    public static BaseCache getInstance() {
        if (sCache == null) {
            throw new RuntimeException("get(Context) never called");
        }
        return sCache;
    }

    public static BaseCache init(Application cxt) {
        if (sCache == null) {
            synchronized (BaseCache.class) {
                if (sCache == null) {
                    sCache = new BaseCache(cxt);
                }
            }
        }
        return sCache;
    }

    /********************* 同步锁双重检测机制实现单例模式（懒加载）********************/

    /**
     * hashMap是线程不安全的，做全局缓存时，用锁来保证存储值
     */
    private ConcurrentHashMap<String, Object> mConfigs = new ConcurrentHashMap<>();
    private ReentrantLock mLock = new ReentrantLock();

    public void put(String key, Object value){
        mLock.lock();
        mConfigs.put(key,value);
        mLock.unlock();
    }

    public Object get(String key){
        mLock.lock();
        Object object = mConfigs.get(key);
        mLock.unlock();
        return object;
    }



    /*********************************************  定义常用方法，方便后续其他地方要用到  **********************************************/

    /**
     * 返回游戏的GameID
     * @return
     */
    public String getGameId() {
        return (String) get(KeyConfig.GAME_ID);
    }

    /**
     * 返回游戏的GameName
     * @return
     */
    public String getGameName() {
        return (String) get(KeyConfig.GAME_NAME);
    }

    /**
     * 返回游戏的GameKey
     * @return
     */
    public String getGameKey() {
        return (String) get(KeyConfig.GAME_KEY);
    }


    /**
     * 返回项目SDK类型,默认返回1
     * @return
     */
    public String getSdkType(){
        return TextUtils.isEmpty((String) get(KeyConfig.SDK_TYPE)) ? "1" : (String) get(KeyConfig.SDK_TYPE);
    }

    /**
     * 返回项目SDK类型
     * @return
     */
    public String getSdkName(){
        return (String) get(KeyConfig.SDK_NAME);
    }

    /**
     * 返回项目SDK版本
     * @return
     */
    public String getSdkVersion(){
        return (String)get(KeyConfig.SDK_VERSION);
    }

    /**
     * 返回项目SDK分包标识,
     * 因为分包标识是在出包时通过命名行打进去的,
     * 默认读取配置的配置.
     *
     * 注意：不是以配置文件为准。
     *
     * @return
     */
    public String getSdkTagId(){
        ChannelInfo channelInfo = WalleChannelReader.getChannelInfo(getApplication().getApplicationContext());
        if (channelInfo == null){
            return "1"; //读取默认配置的字段
        }
        return channelInfo.getChannel();
    }

    /**
     * 返回项目SDK域名地址，方便切换域名测试
     * @return
     */
    public String getSdkUrl(){
        return (String)get(KeyConfig.SDK_URL);
    }

    /**
     * 返回渠道的ID
     * @return
     */
    public String getChannelId() {
        return (String)get(KeyConfig.CHANNEL_ID);
    }

    /**
     * 返回渠道的名称
     * @return
     */
    public String getChannelName(){
        return (String)get(KeyConfig.CHANNEL_NAME);
    }

}
