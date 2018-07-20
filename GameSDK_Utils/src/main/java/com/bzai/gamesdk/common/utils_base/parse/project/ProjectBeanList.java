package com.bzai.gamesdk.common.utils_base.parse.project;

import android.text.TextUtils;

import com.bzai.gamesdk.common.utils_base.proguard.ProguardObject;
import com.bzai.gamesdk.common.utils_base.utils.LogUtils;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by bzai on 2018/4/10.
 * <p>
 * Desc: 项目实体类
 *
 * 只提供get方法，不提供set
 */

public class ProjectBeanList extends ProguardObject{

    private List<ProjectBean> project; //注意解析的名字要跟文件一致，不然会导致解析错误

    public List<ProjectBean> getProject() {
        return project;
    }

    public void setProject(List<ProjectBean> project) {
        this.project = project;
    }

    public static class ProjectBean extends ProguardObject {

        private static final String TAG = "ProjectBean";
        /**
         * 反射插件的单例模式方法
         * 返回的插件可能为空
         *
         * @return
         */
        public Project invokeGetInstance() {
            Project p = null;
            Class<?> glass = null;
            if (TextUtils.isEmpty(class_name)) {
                LogUtils.debug_w(TAG, "invokeGetInstance: the class_name is blank");
                return p;
            }
            try {
                glass = Class.forName(class_name);
            } catch (ClassNotFoundException e) {
                LogUtils.debug_w(TAG, "invokeGetInstance: " + "do not find " + class_name);
            }
            try {
                //尝试调用getInstance
                Method m = glass
                        .getDeclaredMethod("getInstance", new Class<?>[]{});
                m.setAccessible(true);
                p = (Project) m.invoke(null, new Object[]{});
            } catch (NoSuchMethodException e1) {
                //调用getInstance失败后，尝试new其对象
                try {
                    p = (Project) glass.newInstance();
                } catch (Exception exception) {
                    LogUtils.debug_w(TAG, "glass.newInstance(): " + "do not find " + class_name);
                }
            } catch (Exception exception) {
                LogUtils.debug_w(TAG, "glass.getInstance(): " + "do not find " + class_name);
            }

            if (p == null) {
                LogUtils.debug_w(TAG, class_name + " is empty.");
            } else {
                p.projectBean = this;
            }
            return p;
        }

        /**
         * project_name : 项目名称
         * class_name : 项目入口类
         * description : 项目描述
         * version : 版本信息
         */

        private String project_name;
        private String class_name;
        private String description;
        private String version;

        public String getProject_name() {
            return project_name;
        }

        public void setProject_name(String plugin_name) {
            this.project_name = plugin_name;
        }

        public String getClass_name() {
            return class_name;
        }

        public void setClass_name(String class_name) {
            this.class_name = class_name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        @Override
        public String toString() {
            return "ProjectBean{" +
                    " project_name='" + project_name + '\'' +
                    ", class_name='" + class_name + '\'' +
                    ", description='" + description + '\'' +
                    ", version='" + version + '\'' +
                    '}';
        }
    }
}
