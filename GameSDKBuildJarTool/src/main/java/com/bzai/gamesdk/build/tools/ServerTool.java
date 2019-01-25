package com.bzai.gamesdk.build.tools;

import com.bzai.gamesdk.build.bean.Project;
import com.bzai.gamesdk.build.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;

/**
 * 模拟服务器同步过程
 * 一般项目代码都是放到svn或者git上托管的,模拟服务器存储地址
 */
public class ServerTool {

    public static void DownServerResource(List<Project> projectList, String checkoutDir) throws IOException{

        String root_path = System.getProperty("user.dir");
        Iterator<Project> iterator = projectList.iterator();
        while (iterator.hasNext()){
            Project project = iterator.next();

            // 获取源码目录
            String projectName = project.getName();
            String projectUrl = project.getUrl();
            String projectResource = root_path + File.separator + projectUrl;
//            System.out.println(projectResource);

            // 工作目录
            String destPath = checkoutDir + File.separator + projectName ;
            Path destDirPath = FileSystems.getDefault().getPath(destPath);
//            System.out.println(destDirPath);

            boolean destDirPathExists = Files.exists(destDirPath, new LinkOption[]{LinkOption.NOFOLLOW_LINKS});
            if (destDirPathExists){
                FileUtils.delete(destPath);
            }

            FileUtils.copy(projectResource,destPath,false);

            //设置本地存放路径
            project.setPath(destPath);
        }

    }
}
