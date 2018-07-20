package com.bzai.gamesdk.common.utils_business.cache;

import android.content.Context;
import android.content.SharedPreferences;


import com.bzai.gamesdk.common.utils_base.frame.google.gson.Gson;

import java.util.HashSet;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.MODE_WORLD_READABLE;
import static android.content.Context.MODE_WORLD_WRITEABLE;

/**
 * Created by bzai on 2018/4/10.
 * <p>
 * Desc:
 *
 *  持久化数据缓存类
 *
 */
public class SharePreferencesCache {

    private static SharedPreferences mSharedPreferences;
    private static Gson mGson;
    private static int INVALID_VALUE = -1;

    private Context mContext;
    private String mName = "SDK.SpCache";
    private int mMode;

    /**
     * Initial the preferences manager.
     *
     * @param context The context of the application.
     */
    public SharePreferencesCache(Context context) {
        mContext = context;
        mGson = new Gson();
        mMode = INVALID_VALUE;
    }

    /**
     * Set the mode of the preferences.
     *
     * @param mode The mode of the preferences.
     */
    private SharePreferencesCache setMode(int mode) {
        mMode = mode;
        return this;
    }

    /**
     * Initial the instance of the preferences manager.
     */
    public void init() {
        if (mContext == null) {
            return;
        }

        if (mName.isEmpty()) {
            mName = mContext.getPackageName();
        }

        if (mMode == INVALID_VALUE || (mMode != MODE_PRIVATE && mMode != MODE_WORLD_READABLE
                && mMode != MODE_WORLD_WRITEABLE)) {
            mMode = MODE_PRIVATE;
        }

        mSharedPreferences = mContext.getSharedPreferences(mName, mMode);
    }

    /**
     * Put a String value in the preferences editor.
     *
     * @param key   The name of the preference to modify.
     * @param value The new value for the preference.
     */
    public static void putString(String key, String value) {
        if (mSharedPreferences == null) {
            return;
        }

        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    /**
     * Retrieval a String value from the preferences.
     *
     * @param key      The name of the preference to retrieve.
     * @param defValue Value to return if this preference does not exist.
     * @return Returns the preference values if they exist, or defValues.
     */
    public static String getString(String key, String defValue) {
        if (mSharedPreferences == null) {
            return defValue;
        }
        return mSharedPreferences.getString(key, defValue);
    }

    /**
     * Retrieval a String value from the preferences.
     *
     * @param key The name of the preference to retrieve.
     * @return Returns the preference values if they exist, or defValues.
     */
    public static String getString(String key) {
        return getString(key, "");
    }

    /**
     * Put a set of String values in the preferences editor.
     *
     * @param key    The name of the preference to modify.
     * @param values The set of new values for the preference.
     */
    public static void putStringSet(String key, Set<String> values) {
        if (mSharedPreferences == null) {
            return;
        }

        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putStringSet(key, values);
        editor.apply();
    }

    /**
     * Retrieval a set of String values from the preferences.
     *
     * @param key       The name of the preference to retrieve.
     * @param defValues Values to return if this preference does not exist.
     * @return Returns the preference values if they exist, or defValues.
     */
    public static Set<String> getStringSet(String key, Set<String> defValues) {
        if (mSharedPreferences == null) {
            return defValues;
        }
        return mSharedPreferences.getStringSet(key, defValues);
    }

    /**
     * Retrieval a set of String values from the preferences.
     *
     * @param key The name of the preference to retrieve.
     * @return Returns the preference values if they exist, or defValues.
     */
    public static Set<String> getStringSet(String key) {
        return getStringSet(key, new HashSet<String>());
    }

    /**
     * Put an int value in the preferences editor.
     *
     * @param key   The name of the preference to modify.
     * @param value The new value for the preference.
     */
    public static void putInt(String key, int value) {
        if (mSharedPreferences == null) {
            return;
        }

        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }


    /**
     * Retrieval an int value from the preferences.
     *
     * @param key      The name of the preference to retrieve.
     * @param defValue Value to return if this preference does not exist.
     * @return Returns the preference values if they exist, or defValues.
     */
    public static int getInt(String key, int defValue) {
        if (mSharedPreferences == null) {
            return defValue;
        }
        return mSharedPreferences.getInt(key, defValue);
    }

    /**
     * Retrieval an int value from the preferences.
     *
     * @param key The name of the preference to retrieve.
     * @return Returns the preference values if they exist, or defValues.
     */
    public static int getInt(String key) {
        return getInt(key, 0);
    }

    /**
     * Put a float value in the preferences editor.
     *
     * @param key   The name of the preference to modify.
     * @param value The new value for the preference.
     */
    public static void putFloat(String key, float value) {
        if (mSharedPreferences == null) {
            return;
        }

        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putFloat(key, value);
        editor.apply();
    }

    /**
     * Retrieval a float value from the preferences.
     *
     * @param key      The name of the preference to retrieve.
     * @param defValue Value to return if this preference does not exist.
     * @return Returns the preference values if they exist, or defValues.
     */
    public static float getFloat(String key, float defValue) {
        if (mSharedPreferences == null) {
            return defValue;
        }
        return mSharedPreferences.getFloat(key, defValue);
    }

    /**
     * Retrieval a float value from the preferences.
     *
     * @param key The name of the preference to retrieve.
     * @return Returns the preference values if they exist, or defValues.
     */
    public static float getFloat(String key) {
        return getFloat(key, 0);
    }

    /**
     * Put a long value in the preferences editor.
     *
     * @param key   The name of the preference to modify.
     * @param value The new value for the preference.
     */
    public static void putLong(String key, long value) {
        if (mSharedPreferences == null) {
            return;
        }

        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    /**
     * Retrieval a long value from the preferences.
     *
     * @param key      The name of the preference to retrieve.
     * @param defValue Value to return if this preference does not exist.
     * @return Returns the preference values if they exist, or defValues.
     */
    public static long getLong(String key, long defValue) {
        if (mSharedPreferences == null) {
            return defValue;
        }
        return mSharedPreferences.getLong(key, defValue);
    }

    /**
     * Retrieval a long value from the preferences.
     *
     * @param key The name of the preference to retrieve.
     * @return Returns the preference values if they exist, or defValues.
     */
    public static long getLong(String key) {
        return getLong(key, 0);
    }

    /**
     * Put a boolean value in the preferences editor.
     *
     * @param key   The name of the preference to modify.
     * @param value The new value for the preference.
     */
    public static void putBoolean(String key, boolean value) {
        if (mSharedPreferences == null) {
            return;
        }

        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    /**
     * Retrieval a boolean value from the preferences.
     *
     * @param key      The name of the preference to retrieve.
     * @param defValue Value to return if this preference does not exist.
     * @return Returns the preference values if they exist, or defValues.
     */
    public static boolean getBoolean(String key, boolean defValue) {
        if (mSharedPreferences == null) {
            return defValue;
        }
        return mSharedPreferences.getBoolean(key, defValue);
    }

    /**
     * Retrieval a boolean value from the preferences.
     *
     * @param key The name of the preference to retrieve.
     * @return Returns the preference values if they exist, or defValues.
     */
    public static boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    /**
     * Put a object in the preferences editor.
     *
     * @param key   The name of the preference to modify.
     * @param value The new value for the preference.
     */
    public static void putObject(String key, Object value) {
        if (mGson == null || value == null) {
            return;
        }

        putString(key, mGson.toJson(value));
    }

    /**
     * Retrieval a object from the preferences.
     *
     * @param key  The name of the preference to retrieve.
     * @param type The class of the preference to retrieve.
     * @return Returns the preference values if they exist, or defValues.
     */
    public static <T> T getObject(String key, Class<T> type) {
        if (mSharedPreferences == null || mGson == null) {
            return null;
        }
        return mGson.fromJson(getString(key), type);
    }

    /**
     * Remove a preference from the preferences editor.
     *
     * @param key The name of the preference to remove.
     */
    public static void remove(String key) {
        if (mSharedPreferences == null) {
            return;
        }
        mSharedPreferences.edit().remove(key).apply();
    }

    /**
     * Remove all values from the preferences editor.
     */
    public static void clear() {
        if (mSharedPreferences == null) {
            return;
        }
        mSharedPreferences.edit().clear().apply();
    }

    /**
     * Registers a callback to be invoked when a change happens to a preference.
     *
     * @param listener The callback that will run.
     */
    private static void registerOnChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        if (mSharedPreferences == null) {
            return;
        }
        mSharedPreferences.registerOnSharedPreferenceChangeListener(listener);
    }

    /**
     * Unregisters a previous callback.
     *
     * @param listener The callback that should be unregistered.
     */
    private static void unregisterOnChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        if (mSharedPreferences == null) {
            return;
        }
        mSharedPreferences.unregisterOnSharedPreferenceChangeListener(listener);
    }

}
