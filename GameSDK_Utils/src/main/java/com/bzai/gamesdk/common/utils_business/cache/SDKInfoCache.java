package com.bzai.gamesdk.common.utils_business.cache;

import android.content.Context;
import android.text.TextUtils;

import com.bzai.gamesdk.common.utils_base.utils.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by bzai on 2018/4/10.
 * <p>
 * Desc:
 *
 *   初始化配置文件信息
 */
public class SDKInfoCache {

    private Context mAppContext;
    private ConcurrentHashMap<String, String> mFileStrings = new ConcurrentHashMap<String, String>();

    private static String SDK_Json = "SDKInfo.json";

    /********************* 同步锁双重检测机制实现单例模式（懒加载）********************/

    private static SDKInfoCache sLoader;
    public static SDKInfoCache getDefault(Context cxt) {
        if (sLoader == null) {
            synchronized (SDKInfoCache.class) {
                if (sLoader == null) {
                    sLoader = new SDKInfoCache(cxt.getApplicationContext());
                }
            }
        }
        return sLoader;
    }

    private SDKInfoCache(Context appContext) {
        mAppContext = appContext;
        setConfig(SDK_Json,parseConfig(SDK_Json));
    }

    /********************* 同步锁双重检测机制实现单例模式（懒加载）********************/

    private static HashMap<String, String> mConfigs = new HashMap<String, String>();

    public String get(String key) {
        return mConfigs.get(key);
    }

    public void setConfig(String filePath, HashMap<String, String> configs) {

        LogUtils.debug_d(filePath + "=" + configs.toString());
        if (configs != null){
            mConfigs.putAll(configs);

            //同时将数据存储到YZWBaseCache中
            for (String key: configs.keySet()){
                BaseCache.getInstance().put(key,get(key));
            }
        }

    }

    private HashMap<String, String> parseConfig(String filePath) {
        HashMap<String, String> mConfigs = new HashMap<String, String>();
        String configs = readFile(filePath);
        LogUtils.d(filePath + " = " +configs);
        if (!configs.equals("")) {
            try {
                JSONObject jo = new JSONObject(configs);
                Iterator<?> keys = jo.keys();
                while (keys.hasNext()) {
                    String key = keys.next().toString();
                    mConfigs.put(key, jo.getString(key));
                }
            } catch (JSONException e2) {
                LogUtils.e(e2.toString());
            }
        }
        return mConfigs;
    }

    /**
     * 需要同步锁，公有数据缓存 mFileStrings
     * @param fileName
     * @return
     */
    private synchronized String readFile(String fileName) {
        if (TextUtils.isEmpty(fileName)) {
            return "";
        }

        String content = mFileStrings.get(fileName);
        if (content != null) {
            return content;
        }

        InputStream is = null;
        ByteArrayOutputStream baos = null;
        try {
            is = mAppContext.getAssets().open(fileName);
            byte[] buffer = new byte[1024];
            int readBytes = is.read(buffer);
            baos = new ByteArrayOutputStream(1024);
            while (0 < readBytes) {
                baos.write(buffer, 0, readBytes);
                readBytes = is.read(buffer);
            }
            String s = baos.toString();
            mFileStrings.put(fileName, s);
            return s;
        } catch (IOException e) {
            LogUtils.debug_e(e.toString());
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
            if (null != baos) {
                try {
                    baos.close();
                } catch (IOException e) {
                }
            }
        }

        mFileStrings.put(fileName, "");
        return "";
    }
}
