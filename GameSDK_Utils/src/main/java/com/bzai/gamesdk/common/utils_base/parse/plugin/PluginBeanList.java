package com.bzai.gamesdk.common.utils_base.parse.plugin;

import android.text.TextUtils;

import com.bzai.gamesdk.common.utils_base.proguard.ProguardObject;
import com.bzai.gamesdk.common.utils_base.utils.LogUtils;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by bzai on 2018/4/9.
 * 功能插件的实体基类
 *
 * 只提供get方法，不提供set
 */

public class PluginBeanList extends ProguardObject{

    private List<PluginBean> plugin; //注意解析的名字要跟文件一致，不然会导致解析错误

    public List<PluginBean> getPlugin() {
        return plugin;
    }

    public static class PluginBean extends ProguardObject {

        private static final String TAG = "PluginBean";

        /**
         * 反射插件的单例模式方法
         * 返回的插件可能为空
         *
         * @return
         */
        public Plugin invokeGetInstance() {
            Plugin plugin = null;
            Class<?> glass = null;
            if (TextUtils.isEmpty(class_name)){
                LogUtils.debug_i(TAG, "invokeGetInstance: the class_name is blank");
                return plugin;
            }
            try {
                glass = Class.forName(class_name);
            } catch (ClassNotFoundException e) {
                LogUtils.debug_i(TAG, "invokeGetInstance: " + "do not find " + class_name );
            }
            try {
                //尝试调用getInstance
                Method m = glass.getDeclaredMethod("getInstance", new Class<?>[]{});
                m.setAccessible(true);
                plugin = (Plugin) m.invoke(null, new Object[]{});
            } catch (NoSuchMethodException e1) {
                //调用getInstance失败后，尝试new其对象
                try {
                    plugin = (Plugin) glass.newInstance();
                } catch (Exception exception) {
                    LogUtils.debug_w(TAG, "glass.newInstance(): " + "do not find " + class_name);
                }
            } catch (Exception exception) {
                LogUtils.debug_w(TAG, "glass.getInstance(): " + "do not find " + class_name);
            }

            if (plugin == null) {
                LogUtils.debug_i(TAG, class_name + " is empty.");
            } else {
                plugin.pluginBean = this;
            }
            return plugin;
        }

        /**
         * plugin_name : 插件名称
         * class_name ：插件入口类
         * description : 插件描述
         * version : 插件版本
         */

        private String plugin_name;
        private String class_name;
        private String description;
        private String version;

        public String getPlugin_name() {
            return plugin_name;
        }

        public String getClass_name() {
            return class_name;
        }

        public String getDescription() {
            return description;
        }

        public String getVersion() {
            return version;
        }

        @Override
        public String toString() {
            return "PluginBean{" +
                    " plugin_name='" + plugin_name + '\'' +
                    ", class_name='" + class_name + '\'' +
                    ", description='" + description + '\'' +
                    ", version='" + version + '\'' +
                    '}';
        }
    }
}
