package com.bzai.gamesdk.build;

import com.bzai.gamesdk.build.bean.Project;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

/**
 * 配置文件
 */
public class Config {

    /**
     * android SDK 路径
     */
    public String androidSdkPath;

    /**
     * android target 版本
     */
    public String targetSdkVersion;

    /**
     * 输出jar包的名称
     */
    public String jarName;

    /**
     * 输出jar包的版本号
     */
    public String jarVersion;

    /**
     * 输出jar的路径
     */
    public String outPutPath;

    private static Config mConfig;
    private static byte[] syncObj = new byte[0];

    private Config(){
    }

    public static Config getInstance(){
        if (null == mConfig){
            synchronized (syncObj){
                if (null == mConfig){
                    mConfig = new Config();
                }
            }
        }
        return mConfig;
    }

    /**
     * 加载配置文件
     */
    public void loadConfig() throws IOException{

          //这段代码在IDEA中可以正常读取到
//        Properties properties = new Properties();
//        URL url = ClassLoader.getSystemClassLoader().getResource("config");
//        properties.load(new FileInputStream(url.getFile()));

        String config_path = get_config_file_path("config");
        Properties properties = new Properties();
        properties.load(new FileInputStream(config_path));

        androidSdkPath = properties.getProperty("androidSdkPath");
        targetSdkVersion = properties.getProperty("targetSdkVersion");
        jarName = properties.getProperty("jarName");
        jarVersion = properties.getProperty("jarVersion");
        outPutPath = properties.getProperty("outPutPath");
    }

    /**
     * 解析配置文件的工程配置
     * @return
     * @throws IOException
     */
    public List<Project> getProject() throws IOException{

//        Properties properties = new Properties();
//        URL url = ClassLoader.getSystemClassLoader().getResource("project_list");
//        properties.load(new FileInputStream(url.getFile()));

        String config_path = get_config_file_path("project_list");
        Properties properties = new Properties();
        properties.load(new FileInputStream(config_path));

        List<Project> projectList = new ArrayList<>();
        Set<Object> keySet = properties.keySet();
        for (Object key : keySet){
            String keyStr = (String) key;
            String value = properties.getProperty(keyStr);
            value = value.replaceAll(" ","");
            String[] valueList = value.split(",");
            int revision = 0;
            if (valueList[0].matches("[0-9]*")){
                revision = Integer.parseInt(valueList[0]);
            }
            Project project = new Project();
            project.setVersion(revision);
            project.setName(keyStr);
            project.setUrl(valueList[1]);
            projectList.add(project);
        }
        return projectList;
    }

    public String get_config_file_path(String file_name){

        //在AndroidStudio中用绝对路径读取
        //但是要在Run/Debug configurations 配置Working directory工作目录路径
        String root_path = System.getProperty("user.dir");
        String file_path = root_path + File.separator + "GameSDKBuildJarTool" +
                File.separator + "src"  + File.separator + "main" +
                File.separator + "resources"  + File.separator + file_name;
//        System.out.println(file_path);
        return file_path;
    }

    public String getAndroidSdkPath() {
        return androidSdkPath;
    }

    public void setAndroidSdkPath(String androidSdkPath) {
        this.androidSdkPath = androidSdkPath;
    }

    public String getTargetSdkVersion() {
        return targetSdkVersion;
    }

    public void setTargetSdkVersion(String targetSdkVersion) {
        this.targetSdkVersion = targetSdkVersion;
    }

    public String getJarName() {
        return jarName;
    }

    public void setJarName(String jarName) {
        this.jarName = jarName;
    }

    public String getJarVersion() {
        return jarVersion;
    }

    public void setJarVersion(String jarVersion) {
        this.jarVersion = jarVersion;
    }

    public String getOutPutPath() {
        return outPutPath;
    }

    public void setOutPutPath(String outPutPath) {
        this.outPutPath = outPutPath;
    }
}
