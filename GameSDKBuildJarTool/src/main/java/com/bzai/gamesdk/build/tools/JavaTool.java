package com.bzai.gamesdk.build.tools;

import com.bzai.gamesdk.build.tools.exec.Shell;
import com.bzai.gamesdk.build.utils.Utils;

import javax.tools.JavaCompiler;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JavaTool {


    public static void compile(String sourceCodePath, String classPath, String classFileOutputPath,
                               String logOutputPath) throws Exception{

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

        Path source = FileSystems.getDefault().getPath(sourceCodePath);
        final List<String> javaFilePathList = new ArrayList<>();
        Files.walkFileTree(source, new SimpleFileVisitor<Path>(){

            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                if (file.toString().endsWith(".java")){
                    javaFilePathList.add(file.toAbsolutePath().toString());

                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                return FileVisitResult.CONTINUE;
            }
        });

        //编译日志输出到文件
        FileWriter fileWriter = null;
        StandardJavaFileManager standardJavaFileManager = null;
        boolean compileSucceed = true;
        try {

            if (!Utils.isEmpty(logOutputPath)){
                fileWriter = new FileWriter(logOutputPath,true);
            }

            /**
             * encoding    编译编码
             * jars        需要加载的jar,-classpath选项就是定义class文件的查找目录
             * sourceDir   java源文件存放目录
             * Iterable<String> options = Arrays.asList("-encoding", encoding, "-classpath", jars, "-d", targetDir, "-sourcepath", sourceDir);
             */

            List<String> optionList = new ArrayList<>();
            optionList.addAll(Arrays.asList("-classpath",classPath));
            optionList.addAll(Arrays.asList("-d",classFileOutputPath));
            standardJavaFileManager = compiler.getStandardFileManager(null,null,null);
            Iterable fileObjects = standardJavaFileManager.getJavaFileObjectsFromStrings(javaFilePathList);

            JavaCompiler.CompilationTask compilationTask = compiler.getTask(fileWriter,null,null,optionList,null,fileObjects);
            compileSucceed = compilationTask.call();

        }catch (Exception e){
            e.printStackTrace();

        }finally {
            if (null != fileWriter){
                fileWriter.close();
            }
            if (null != standardJavaFileManager){
                standardJavaFileManager.close();
            }
        }

        if (!compileSucceed){//编译失败
            throw new Exception("Compile .java to .class failed");
        }
    }

    /**
     *
     * 类文件打为jar包。
     *
     * @param classFilesPath
     * @param jarOutputPath
     */
    public static void classFilesToJar(String classFilesPath, String jarOutputPath, String logOutputPath) throws Exception{

        List<String> arguments = new ArrayList<String>();
        arguments.add("jar");
        arguments.add("cvf");
        arguments.add(jarOutputPath);
        arguments.add("-C");
        arguments.add(classFilesPath);
        arguments.add(".");
        Shell.execute(arguments, logOutputPath);
    }
}
