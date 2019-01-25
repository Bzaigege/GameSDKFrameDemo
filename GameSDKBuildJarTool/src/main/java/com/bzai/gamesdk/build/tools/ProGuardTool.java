package com.bzai.gamesdk.build.tools;

import com.bzai.gamesdk.build.tools.exec.Shell;
import proguard.Configuration;
import proguard.ConfigurationParser;
import proguard.ProGuard;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bzai on 2018/04/20.
 */
public class ProGuardTool {

    /**
     * 混淆jar文件
     *
     * @param inputJar 需要混淆的jar。
     * @param classPath 需要混淆的jar所引用的class path.
     * @param proguardConfigFilePath 混淆配置文件路径。
     * @param mappingFileOutputPath mapping 文件输出路径。
     * @param outputJar 混淆之后的jar的输出路径。
     */
    public static void run(String inputJar, String classPath, String proguardConfigFilePath,
                           String mappingFileOutputPath, String outputJar)  {
        String[] commandLineArgs = new String[9];
        commandLineArgs[0] = "@"+proguardConfigFilePath;
        commandLineArgs[1] = "-injars";
        commandLineArgs[2] = inputJar;
        commandLineArgs[3] = "-outjars";
        commandLineArgs[4] = outputJar;
        commandLineArgs[5] = "-printmapping";
        commandLineArgs[6] = mappingFileOutputPath;
        commandLineArgs[7] = "-libraryjars";
        commandLineArgs[8] = classPath;
        Configuration var1 = new Configuration();
        for (int i=0;i<commandLineArgs.length;i++){
            System.out.printf(commandLineArgs[i]);
        }

        try {
            ConfigurationParser var2 = new ConfigurationParser(commandLineArgs, System.getProperties());
            try {
                var2.parse(var1);
            } finally {
                var2.close();
            }
            (new ProGuard(var1)).execute();
        } catch (Exception var7) {
            if(var1.verbose) {
                var7.printStackTrace();
            } else {
                System.err.println("Error: " + var7.getMessage());
            }
        }
    }

    /**
     * 混淆jar文件
     *
     * @param proguardToolPath 混淆工具的路径。
     * @param inputJar 需要混淆的jar。
     * @param classPath 需要混淆的jar所引用的class path.
     * @param proguardConfigFilePath 混淆配置文件路径。
     * @param mappingFileOutputPath mapping 文件输出路径。
     * @param proguardLogOutputPath 混淆日志输出文件。
     * @param outputJar 混淆之后的jar的输出路径。
     *
     * @throws Exception
     */
    public static void run(String proguardToolPath, String inputJar, String classPath, String proguardConfigFilePath,
                           String mappingFileOutputPath, String proguardLogOutputPath, String outputJar) throws Exception {
        List<String> commandLineArgs = new ArrayList<String>();
        commandLineArgs.add("java");
        commandLineArgs.add("-jar");
        commandLineArgs.add(proguardToolPath);
        commandLineArgs.add("-injars");
        commandLineArgs.add(inputJar);
        commandLineArgs.add("-outjars");
        commandLineArgs.add(outputJar);
        commandLineArgs.add("-printmapping");
        commandLineArgs.add(mappingFileOutputPath);
        commandLineArgs.add("@"+proguardConfigFilePath);
        commandLineArgs.add("-libraryjars");
        commandLineArgs.add(classPath);
        Shell.execute(commandLineArgs, proguardLogOutputPath);
    }

}
