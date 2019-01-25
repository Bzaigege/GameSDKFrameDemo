package com.bzai.gamesdk.build.bean;

import java.io.File;

/**
 * Project 配置类
 */
public class Project {

    /**
     * 源码相对于项目所在路径。
     */
    public static final String JAVA_RELATIVE_PATH = "src" + File.separator
            + "main" + File.separator + "java";

    /**
     * res资源文件相对于项目所在路径。
     */
    public static final String RES_RELATIVE_PATH = "src" + File.separator
            + "main" + File.separator + "res";

    /**
     * assets文件相对于项目所在路径。
     */
    public static final String ASSETS_RELATIVE_PATH = "src" + File.separator
            + "main" + File.separator + "assets";

    /**
     * libs相对于项目所在路径。
     */
    public static final String LIBS_RELATIVE_PATH = "libs";

    /**
     * Android manifest相对于项目所在路径。
     */
    public static final String ANDROID_MANIFEST_RELATIVE_PATH = "src" + File.separator
            + "main" + File.separator + "AndroidManifest.xml";

    /**
     * jni libs相对于项目所在路径。
     */
    public static final String JNI_LIBS_RELATIVE_PATH = "src" + File.separator
            + "main" + File.separator + "jniLibs";

    /**
     * jni libs相对于项目所在路径。
     */
    public static final String MAIN_FILE_RELATIVE_PATH = "src" + File.separator + "main";

    /**
     * unknown文件相对于项目所在路径。
     */
    public static final String UNKNOWN_FILE_RELATIVE_PATH = "src" + File.separator
            + "main" + File.separator + "unknown";

    /**
     * project src file name.
     */
    public static final String SRC_FILE = "src";

    /**
     * project assets file name.
     */
    public static final String ASSETS_FILE = "assets";

    /**
     * project res file name.
     */
    public static final String RES_FILE = "res";

    /**
     * project libs file name.
     */
    public static final String LIBS_FILE = "libs";

    /**
     * project libs file name.
     */
    public static final String JNI_LIBS_FILE = "jniLibs";

    /**
     * project unknown file name.
     */
    public static final String UNKNOWN_FILE = "unknown";

    /**
     * app file name.
     */
    public static final String APP_FILE = "app";

    /**
     * project android manifest file name.
     */
    public static final String ANDROID_MANIFEST_FILE = "AndroidManifest.xml";

    /**
     * 项目名称
     */
    public String name;

    /**
     * 打包路径
     */
    public String path;

    /**
     * 服务器版本
     */
    public int version;

    /**
     * 服务器地址
     */
    public String url;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
