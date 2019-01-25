package com.bzai.gamesdk.build;

import com.android.manifmerger.ManifestMerger2;
import com.android.manifmerger.MergingReport;
import com.android.manifmerger.XmlDocument;
import com.android.utils.StdLogger;
import com.bzai.gamesdk.build.bean.ErrorMsg;
import com.bzai.gamesdk.build.bean.Project;
import com.bzai.gamesdk.build.tools.JavaTool;
import com.bzai.gamesdk.build.tools.ProGuardTool;
import com.bzai.gamesdk.build.tools.ServerTool;
import com.bzai.gamesdk.build.utils.FileUtils;
import com.bzai.gamesdk.build.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.List;

/**
 * 混淆jarTask
 */
public class BuildJarTask implements Runnable{

    @Override
    public void run() {

        //创建输出路径
        String outputPath;
        try {

            Config.getInstance().loadConfig();
            outputPath = Config.getInstance().getOutPutPath();
            FileUtils.createDirectories(outputPath,true);

        }catch (Exception e){
            e.printStackTrace();
            return;
        }

        //同步资源
        List<Project> projectList;
        try {
            projectList = Config.getInstance().getProject();
            if (null == projectList || projectList.isEmpty()){
                System.out.println("project list is empty");
                return;
            }

            String workspacePath = outputPath + File.separator + "workspace";
            FileUtils.createDirectory(workspacePath);

            ServerTool.DownServerResource(projectList,workspacePath);

        }catch (Exception e){
            e.printStackTrace();
            return;
        }

        //buildJar
        String jarName = Config.getInstance().getJarName();
        String jarVersion = Config.getInstance().getJarVersion();
        String buildJar = jarName + "_" + jarVersion;

        String jarOutputPath = outputPath + File.separator + "jar_out";
        String jarFileOutputPath = jarOutputPath + File.separator + buildJar + ".jar";
        ErrorMsg errorMsg = buildJar(projectList, jarFileOutputPath);
        if (errorMsg.code != Utils.OK){
            System.out.println("Tips:" + errorMsg.getMessage() + "\n");
            errorMsg.e.printStackTrace();
            return;
        }


        //build resource file.
        String resourceOutputPath = outputPath + File.separator + "resource";
        try{
            FileUtils.createDirectory(resourceOutputPath);
            buildResourceFile(resourceOutputPath, projectList);
        }catch (Exception e){
            e.printStackTrace();
            return;
        }

        System.out.println("\n");
        System.out.println("SDK输出路径：" + jarFileOutputPath);
        System.out.println("资源文件输出路径：" + resourceOutputPath);

    }


    /**
     * 编译生成混淆jar包过程
     * @param projectList 工程配置
     * @param jarOutputPath jar包输出路径
     * @return
     */
    private ErrorMsg buildJar(List<Project> projectList, String jarOutputPath){

        File jarFile = new File(jarOutputPath);
        String outputPath = jarFile.getParent();

        //创建一个Temp工程
        Project tmpBuildProject = new Project();
        String projectName = "tmpBuildProject";
        String tmpBuildProjectPath = outputPath + File.separator + projectName;
        tmpBuildProject.setName(projectName);
        tmpBuildProject.setPath(tmpBuildProjectPath);

        //创建temp工程目录，Java 和 libs
        String projectSrcPath = tmpBuildProjectPath + File.separator + Project.JAVA_RELATIVE_PATH;
        try{
            FileUtils.createDirectoriesIfNonExists(outputPath);
            FileUtils.createDirectoriesIfNonExists(tmpBuildProjectPath);
            FileUtils.createDirectoriesIfNonExists(projectSrcPath);
        }catch (Exception e){
            return new ErrorMsg(Utils.ERROR, e.getMessage(), e);
        }

        //将各个项目的java文件下的源文件拷贝到 tmpBuildProject 的src文件下。
        try{
            for (Project project : projectList){
                String tmpProjectSrcPath = project.getPath() + File.separator + Project.JAVA_RELATIVE_PATH;
                if (FileUtils.exists(tmpProjectSrcPath)){
                    FileUtils.copy(tmpProjectSrcPath, projectSrcPath, false);
                }
            }
        }catch (Exception e){
            return new ErrorMsg(Utils.ERROR, e.getMessage(), e);
        }

        // .java and .jar compile to .class
        String classPath = getClasspath(tmpBuildProject,projectList);
        String classFilesOutputPath = tmpBuildProjectPath + File.separator + "classes";
        try {
            FileUtils.createDirectory(classFilesOutputPath);
            JavaTool.compile(projectSrcPath, classPath, classFilesOutputPath, null);

        }catch (Exception e){
            return new ErrorMsg(Utils.ERROR, "构建SDK-编译.java to .class出错", e);
        }

        // .class compile to .jar
        String noProguardJar = outputPath + File.separator + "no_proguard.jar";
        try{
            JavaTool.classFilesToJar(classFilesOutputPath, noProguardJar, null);
        }catch (Exception e){
            return new ErrorMsg(Utils.ERROR, "构建SDK-编译.class打包jar出错", e);
        }

        //proguard jar
        try{

            String mapping = outputPath + File.separator + "proguard_mapping.txt";
            String logging = outputPath + File.separator + "proguard_log.txt";
            String root_path = System.getProperty("user.dir");
            String proJarPath = root_path + File.separator + "GameSDKBuildJarTool" + File.separator + "libs"
                                + File.separator + "proguard.jar";
            String proguardConfigFilePath = Config.getInstance().get_config_file_path("proguard_config.pro");
            ProGuardTool.run(proJarPath, noProguardJar, classPath, proguardConfigFilePath, mapping, logging, jarOutputPath);

        }catch (Exception e){
            return new ErrorMsg(Utils.ERROR, "构建SDK-混淆jar出错", e);
        }

        return new ErrorMsg(Utils.OK, "ok");
    }

    /**
     * 合并工程资源文件过程
     * @param resourceOutputPath
     * @param projectList
     * @throws IOException
     * @throws InterruptedException
     * @throws ManifestMerger2.MergeFailureException
     */
    public void buildResourceFile(String resourceOutputPath, List<Project> projectList)
            throws IOException, InterruptedException, ManifestMerger2.MergeFailureException {

        /*copy libs,assets,res to library project*/
        String libsPath = resourceOutputPath + File.separator + Project.LIBS_FILE;
        String assetsPath = resourceOutputPath + File.separator + Project.ASSETS_FILE;
        final String resPath = resourceOutputPath + File.separator + Project.RES_FILE;
        String jniLibsFilePath = resourceOutputPath + File.separator + Project.JNI_LIBS_FILE;
        for (Project project : projectList){
            /*copy assets*/
            String projectAssetsPath = project.getPath() + File.separator + Project.ASSETS_RELATIVE_PATH;
            if (FileUtils.exists(projectAssetsPath)){
                FileUtils.createDirectoryIfNonExists(assetsPath);
                FileUtils.copy(projectAssetsPath, assetsPath, false);
            }

            /*copy jni libs*/
            String projectJniLibsPath = project.getPath() + File.separator + Project.JNI_LIBS_RELATIVE_PATH;
            if (FileUtils.exists(projectJniLibsPath)){
                FileUtils.createDirectoryIfNonExists(jniLibsFilePath);
                FileUtils.copy(projectJniLibsPath, jniLibsFilePath, false);
            }

            /*copy libs*/
            String projectLibsPath = project.getPath() + File.separator + Project.LIBS_RELATIVE_PATH;
            if (FileUtils.exists(projectLibsPath)){
                FileUtils.createDirectoryIfNonExists(libsPath);
                FileUtils.copy(projectLibsPath, libsPath, false);
            }

            /*copy res*/
            final String projectResPath = project.getPath() + File.separator + Project.RES_RELATIVE_PATH;
            if (FileUtils.exists(projectResPath)){
                FileUtils.createDirectoryIfNonExists(resPath);
                FileUtils.copy(projectResPath, resPath, false);
            }

            //manifest merge
            String projectManifestPath = project.getPath() + File.separator + Project.ANDROID_MANIFEST_RELATIVE_PATH;
            String mainManifestPath = resourceOutputPath + File.separator + Project.ANDROID_MANIFEST_FILE;
            if (!FileUtils.exists(mainManifestPath)){
                FileUtils.copy(projectManifestPath, mainManifestPath, true);
            }else{
                StdLogger stdLogger = new StdLogger(StdLogger.Level.ERROR);
                ManifestMerger2.Invoker manifestMerger = ManifestMerger2.newMerger(new File(mainManifestPath),
                        stdLogger, ManifestMerger2.MergeType.APPLICATION);
                manifestMerger.addLibraryManifest(new File(projectManifestPath));
                manifestMerger.withFeatures(ManifestMerger2.Invoker.Feature.REMOVE_TOOLS_DECLARATIONS);
                MergingReport mergingReport = manifestMerger.merge();
                XmlDocument xmlDocument = mergingReport.getMergedDocument().get();

                Files.write(FileSystems.getDefault().getPath(mainManifestPath),
                        xmlDocument.prettyPrint().getBytes("UTF-8"), StandardOpenOption.WRITE);
                switch (mergingReport.getResult()) {
                    case WARNING:
                        // fall through since these are just warnings.
                        mergingReport.log(stdLogger);
                        break;
                    case SUCCESS:
                        break;
                    case ERROR:
                        mergingReport.log(stdLogger);
                        throw new RuntimeException(mergingReport.getReportString());
                    default:
                        throw new RuntimeException("Unhandled result type : "
                                + mergingReport.getResult());
                }
            }
        }

//        /*如果jar文件中存在assets资源文件，需要jar中的将assets文件提取出来*/
//        List<String> jarFileList = FileUtils.getFileList(libsPath, ".jar");
//        for (String jarFilePath : jarFileList){
//            File jarFile = new File(jarFilePath);
//            String unzipToFilePath = libsPath +File.separator
//                    + jarFile.getName().substring(0, jarFile.getName().lastIndexOf("."));
//            Utils.jarFileUnzip(jarFilePath, unzipToFilePath);
//            String assetsFilePath = unzipToFilePath + File.separator + Project.ASSETS_FILE;
//            if (FileUtils.exists(assetsFilePath)){
//                String outputPathAssetsPath = resourceOutputPath + File.separator + Project.ASSETS_FILE;
//                FileUtils.createDirectoryIfNonExists(outputPathAssetsPath);
//                FileUtils.move(assetsFilePath, outputPathAssetsPath, false);
//
//                /*删除原来的jar，重新压缩一个删除assets的jar文件*/
//                FileUtils.delete(jarFilePath);
//                JavaTool.classFilesToJar(unzipToFilePath, jarFilePath, null);
//
//                FileUtils.delete(unzipToFilePath);
//            }else{
//                FileUtils.delete(unzipToFilePath);
//            }
//        }
    }

    private String getClasspath(Project project, List<Project> projectList){

        String classpath = "";

        //获取工程jar路径
        for (Project dependProject : projectList){
            String projectPath = dependProject.getPath();
            String projectLibsPath = projectPath + File.separator + Project.LIBS_RELATIVE_PATH;
            if (FileUtils.exists(projectLibsPath)){
                String jarPathSet = Utils.getJarPathSet(projectLibsPath,Utils.OS_SEMICOLON);
                if (!Utils.isEmpty(jarPathSet)){
                    if (!Utils.isEmpty(classpath)){
                        classpath = classpath + Utils.OS_SEMICOLON;
                    }
                    classpath = classpath + jarPathSet;
                }
            }
        }

        //获取工程源码路径
        String projectJavaPath = project.getPath() + File.separator + Project.JAVA_RELATIVE_PATH;
        if (FileUtils.exists(projectJavaPath)){
            if (!Utils.isEmpty(classpath)){
                classpath = classpath + Utils.OS_SEMICOLON;
            }
            classpath = classpath + projectJavaPath;
        }

        //获取SDK版本 android.jar 路径
        if (!Utils.isEmpty(classpath)){
            classpath = classpath + Utils.OS_SEMICOLON;
        }
        classpath = classpath + getMinSdkVersionPath();
        return classpath;
    }

    /**
     * 获取Android sdk jar的存放路径
     *
     * @return
     */
    private String getMinSdkVersionPath(){
        String androidSDKPath = Config.getInstance().getAndroidSdkPath();
        String androidJarPath = androidSDKPath + File.separator +"platforms"
                + File.separator+ Config.getInstance().getTargetSdkVersion()+File.separator+"android.jar";
        return androidJarPath;
    }
}
