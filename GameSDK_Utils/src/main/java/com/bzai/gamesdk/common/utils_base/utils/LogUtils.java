package com.bzai.gamesdk.common.utils_base.utils;


import com.bzai.gamesdk.common.utils_base.frame.logger.AndroidLogAdapter;
import com.bzai.gamesdk.common.utils_base.frame.logger.FormatStrategy;
import com.bzai.gamesdk.common.utils_base.frame.logger.Logger;
import com.bzai.gamesdk.common.utils_base.frame.logger.PrettyFormatStrategy;

/**
 * Created by bzai on 2018/04/09.
 */

public class LogUtils {

    public static final String LOG_TAG = "SYSDK";

    public static boolean DEBUG_VERSION = false;

    /**
     * 设置日志开关模式
     * @param isDebugLog
     */
    public static void setDebugLogModel(boolean isDebugLog){
        DEBUG_VERSION = isDebugLog;
    }

    static {

        FormatStrategy formatStrategy = null;
        if (DEBUG_VERSION) {
            formatStrategy = PrettyFormatStrategy.newBuilder()
                    .showThreadInfo(true)
                    .methodCount(5)
                    .tag(LOG_TAG)
                    .build();
        } else {
            formatStrategy = PrettyFormatStrategy.newBuilder()
                    .methodCount(5)
                    .methodOffset(2)
                    .showThreadInfo(false)
                    .tag(LOG_TAG)
                    .build();
        }
        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy));
    }

    private LogUtils() {

    }


//    --------------------------------------------以下事件输出不区分环境------------------------------------------


    public static void i(Object o) {
        i(null, String.valueOf(o));
    }

    public static void i(String tag, Object o) {
        if (tag == null)
            Logger.i(String.valueOf(o));
        else
            Logger.t(tag).i(String.valueOf(o));
    }

    public static void d(Object o) {
        d(null, o);
    }

    public static void d(String tag, Object o) {
        if (tag == null)
            Logger.d(o);
        else
            Logger.t(tag).d(o);
    }


    public static void w(Object o) {
        w(null, String.valueOf(o));
    }

    public static void w(String tag, Object o) {
        if (tag == null)
            Logger.w(String.valueOf(o));
        else
            Logger.t(tag).w(String.valueOf(o));
    }

    public static void e(Object o) {
        e(null, String.valueOf(o));
    }

    public static void e(String tag, Object o) {
        if (tag == null)
            Logger.e(String.valueOf(o));
        else
            Logger.t(tag).e(String.valueOf(o));
    }

    public static void json(String json) {
        json(null, json);
    }

    public static void json(String tag, String json) {
        if (tag == null)
            Logger.json(json);
        else
            Logger.t(tag).json(json);
    }


//    --------------------------------------------以下事件仅在debug模式下进行输出------------------------------------------

    public static void debug_i(Object o) {
        if (DEBUG_VERSION && o != null) {
            debug_i(null, String.valueOf(o));
        }
    }

    public static void debug_i(String tag, Object o) {
        if (DEBUG_VERSION && o != null) {
            if (tag == null)
                Logger.i(String.valueOf(o));
            else
                Logger.t(tag).i(String.valueOf(o));
        }
    }

    public static void debug_d(Object o) {
        if (DEBUG_VERSION && o != null) {
            debug_d(null, o);
        }
    }

    public static void debug_d(String tag, Object o) {
        if (DEBUG_VERSION && o != null) {
            if (tag == null)
                Logger.d(o);
            else
                Logger.t(tag).d(o);
        }
    }


    public static void debug_w(Object o) {
        if (DEBUG_VERSION && o != null) {
            debug_w(null, String.valueOf(o));
        }
    }

    public static void debug_w(String tag, Object o) {
        if (DEBUG_VERSION && o != null) {
            if (tag == null)
                Logger.w(String.valueOf(o));
            else
                Logger.t(tag).w(String.valueOf(o));
        }
    }

    public static void debug_e(Object o) {
        if (DEBUG_VERSION && o != null) {
            debug_e(null, String.valueOf(o));
        }
    }

    public static void debug_e(String tag, Object o) {
        if (DEBUG_VERSION && o != null) {
            if (tag == null)
                Logger.e(String.valueOf(o));
            else
                Logger.t(tag).e(String.valueOf(o));
        }
    }

    public static void debug_json(String json) {
        if (DEBUG_VERSION && json != null) {
            debug_json(null, json);
        }
    }

    public static void debug_json(String tag, String json) {
        if (DEBUG_VERSION && json != null) {
            if (tag == null)
                Logger.json(json);
            else
                Logger.t(tag).json(json);
        }
    }


}
