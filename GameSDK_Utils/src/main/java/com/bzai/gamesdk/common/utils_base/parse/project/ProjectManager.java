package com.bzai.gamesdk.common.utils_base.parse.project;

import android.content.Context;
import android.text.TextUtils;

import com.bzai.gamesdk.common.utils_base.frame.google.gson.Gson;
import com.bzai.gamesdk.common.utils_base.utils.FileUtils;
import com.bzai.gamesdk.common.utils_base.utils.LogUtils;

import java.util.HashMap;
import java.util.Set;

/**
 * Created by bzai on 2018/4/10.
 * <p>
 * Desc: 项目管理类
 */
public class ProjectManager {

    private static final String TAG = "ProjectManager";
    private static String PROJECT_CONFIG = "Project_config.txt";

    private static Project project;
    private HashMap<String, ProjectBeanList.ProjectBean> projectBeans = new HashMap<>();

    /********************* 同步锁双重检测机制实现单例模式（懒加载）********************/
    private volatile static ProjectManager projectManager;
    public static ProjectManager init(Context context) {
        if (projectManager == null) {
            synchronized (ProjectManager.class) {
                if (projectManager == null) {
                    projectManager = new ProjectManager(context);
                }
            }
        }
        return projectManager;
    }

    public static ProjectManager getInstance() {
        return projectManager;
    }
    /********************* 同步锁双重检测机制实现单例模式 ********************/

    private ProjectManager(Context context) {
        parse(context, PROJECT_CONFIG);
    }

    private void parse(Context context, String pluginFilePath) {
        //从配置文件中，读取插件配置
        StringBuilder projectContent = FileUtils.readAssetsFile(context, pluginFilePath);
        String strProjectContent = String.valueOf(projectContent);

        //进行解析
        Gson gson = new Gson();
        if (!TextUtils.isEmpty(strProjectContent)) {
            try {

                ProjectBeanList projectBeanList = gson.fromJson(strProjectContent, ProjectBeanList.class);
                if (projectBeanList.getProject() != null && projectBeanList.getProject().size() != 0) {
                    //如果解析结果无误，载入到listPluginBean中去
                    for (ProjectBeanList.ProjectBean projectBean : projectBeanList.getProject()) {
                        projectBeans.put(projectBean.getProject_name(), projectBean);
                    }
                    //打印解析结果
                    LogUtils.debug_i(TAG, PROJECT_CONFIG +" parse: \n" + projectBeans.toString());
                } else {
                    //解析结果出错
                    LogUtils.e(TAG, PROJECT_CONFIG + " parse error.");
                }

            } catch (Exception e) {
                //解析结果出错
                LogUtils.e(TAG, PROJECT_CONFIG + " parse exception.");
                e.printStackTrace();
            }
        }else {
            LogUtils.e(TAG, PROJECT_CONFIG + " parse is blank.");
        }

    }


    private boolean hasLoaded;
    private static HashMap<String, Project> ProjectLists = new HashMap<String, Project>();


    /**
     * 加载所有的Project,可能存在多个项目
     */
    public synchronized void loadAllProjects() {
        if (hasLoaded) {
            return;
        }
        HashMap<String, ProjectBeanList.ProjectBean> entries = projectBeans;
        Set<String> set = entries.keySet();
        for (String key : set) {
            loadProject(key);
        }
        LogUtils.debug_i(TAG, "loadAllProjects:" + ProjectLists.toString());
        hasLoaded = true;
    }


    /**
     * 加载一个项目，返回的Project可能为空
     *
     * @param projectName
     * @return
     * @throws RuntimeException
     */
    private Project loadProject(String projectName) throws RuntimeException {

        // 1.查看从配置文件中读取的插件列表，是否存在此插件
        HashMap<String, ProjectBeanList.ProjectBean> entries = projectBeans;
        ProjectBeanList.ProjectBean projectBean = entries.get(projectName);
        if (projectBean == null) {
            LogUtils.debug_i(TAG, "The project [" +  projectName + "] does not exists in " + PROJECT_CONFIG);
            return null;
        }
        Project project = null;
        // 2.调用其单例模式方法
        project = projectBean.invokeGetInstance();
        if (project != null) {
            // 3.反射初始化插件
            project.initProject();
            // 4.将已加载好的插件，添加到插件列表中去
            ProjectLists.put(projectName, project);
        }
        return project;
    }


    /**
     * 获取特定项目
     * 可能为空
     *
     * @param projectName
     * @return
     */
    public Project getProject(String projectName) {
        if (!hasLoaded) {
            LogUtils.debug_i(TAG, "getProject: " + projectName + "Project not loaded yet");
            return null;
        }
        Project project = null;
        HashMap<String, Project> entries = ProjectLists;
        project = entries.get(projectName);
        return project;
    }


    /**
     * 加载当前的project项目
     *
     */
    public void loadProject(){

        ProjectBeanList.ProjectBean projectBean = projectBeans.get("project");
        if (projectBean == null){
            LogUtils.debug_d(TAG, "The project does not exists in " + PROJECT_CONFIG);
            return;
        }

        Project project = null;
        project = projectBean.invokeGetInstance();
        if (project != null){
            project.initProject();
            this.project = project;
        }
    }

    /**
     * 返回当前的project项目
     * @return
     */
    public Project getProject(){
        return project;
    }



}
