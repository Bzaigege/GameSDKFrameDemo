package com.bzai.gamesdk.common.utils_base.frame.walle;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.text.TextUtils;

import com.bzai.gamesdk.common.utils_base.frame.walle.payload_reader.ChannelInfo;
import com.bzai.gamesdk.common.utils_base.frame.walle.payload_reader.ChannelReader;

import java.io.File;
import java.util.Map;

/**
 * 集成美团的打多渠道标识方案，相关内容详看博客及文章，
 * 目前在SDK中是读取,写入是通过命令行写入
 *
 * https://tech.meituan.com/android-apk-v2-signature-scheme.html
 * https://github.com/Meituan-Dianping/walle
 */
public final class WalleChannelReader {
    private WalleChannelReader() {
        super();
    }

    /**
     * get channel
     *
     * @param context context
     * @return channel, null if not fount
     */
    public static String getChannel(final Context context) {
        return getChannel(context, null);
    }

    /**
     * get channel or default
     *
     * @param context context
     * @param defaultChannel default channel
     * @return channel, default if not fount
     */
    public static String getChannel(final Context context, final String defaultChannel) {
        final ChannelInfo channelInfo = getChannelInfo(context);
        if (channelInfo == null) {
            return defaultChannel;
        }
        return channelInfo.getChannel();
    }

    /**
     * get channel info (include channle & extraInfo)
     *
     * @param context context
     * @return channel info
     */
    public static ChannelInfo getChannelInfo(final Context context) {
        final String apkPath = getApkPath(context);
        if (TextUtils.isEmpty(apkPath)) {
            return null;
        }
        return ChannelReader.get(new File(apkPath));
    }

    /**
     * get value by key
     *
     * @param context context
     * @param key     the key you store
     * @return value
     */
    public static String get(final Context context, final String key) {
        final Map<String, String> channelMap = getChannelInfoMap(context);
        if (channelMap == null) {
            return null;
        }
        return channelMap.get(key);
    }

    /**
     * get all channl info with map
     *
     * @param context context
     * @return map
     */
    public static Map<String, String> getChannelInfoMap(final Context context) {
        final String apkPath = getApkPath(context);
        if (TextUtils.isEmpty(apkPath)) {
            return null;
        }
        return ChannelReader.getMap(new File(apkPath));
    }

    private static String getApkPath(final Context context) {
        String apkPath = null;
        try {
            final ApplicationInfo applicationInfo = context.getApplicationInfo();
            if (applicationInfo == null) {
                return null;
            }
            apkPath = applicationInfo.sourceDir;
        } catch (Throwable e) {
        }
        return apkPath;
    }
}
