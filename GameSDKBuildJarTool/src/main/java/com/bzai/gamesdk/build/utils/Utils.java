package com.bzai.gamesdk.build.utils;

import java.io.File;

public class Utils {


    public static final int OK = 0;

    public static final int ERROR = 10001;

    private static String OS = System.getProperty("os.name").toLowerCase();


    /**
     * Linux 和 Windows分号分别使用的是":"和";"。
     */
    public static String OS_SEMICOLON = isWindows()?";":":";


    public static boolean isWindows() {
        return (OS.indexOf("win") >= 0);
    }

    public static boolean isEmpty(String str){
        if (null != str && !"".equals(str)){
            return false;
        }
        return true;
    }


    /**
     * Get jar file path list string, use the delimiter to splice.
     *
     * @param libsPath
     * @return
     */
    public static String getJarPathSet(String libsPath, String delimiter){

        File file = new File(libsPath);
        String jarPathSet = "";
        File[] files = file.listFiles();
        for (int i = 0; i < files.length ; i++) {
            File libFile = files[i];
            if (libFile.getName().endsWith(".jar")){
                if (!isEmpty(jarPathSet)){
                    jarPathSet = jarPathSet + delimiter;
                }
                jarPathSet = jarPathSet + libFile.getAbsolutePath();
            }
        }

        return jarPathSet;
    }
}
