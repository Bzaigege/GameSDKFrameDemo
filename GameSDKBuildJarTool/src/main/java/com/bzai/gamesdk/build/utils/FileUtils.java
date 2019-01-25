package com.bzai.gamesdk.build.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

/**
 * 操作文件类
 */
public class FileUtils {


    private static final String TAG = "FileUtils";

    public static void fileCopy(String srcPath, String destPath, boolean isReplaceExisting) throws IOException {
        Path source = FileSystems.getDefault().getPath(srcPath);
        Path destination = FileSystems.getDefault().getPath(destPath);
        if (exists(destPath)){
            if (isReplaceExisting){
                Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
            }
        }else{
            Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    /**
     * smali file copy.
     *
     * @param sourcePath
     * @param destPath
     * @param filePathList
     *      如果需要拷贝的文件和 filePathList 文件列表路径下的某个文件的有重复的话，
     *      拷贝时直接拷贝覆盖 filePathList 文件列表路径下已经存在的文件。
     * @throws IOException
     */
    public static void smaliFileCopy(final String appFirstSmaliPath, final String sourcePath, final String destPath,
                                     final List<String> filePathList, final List<String> putToMainDexClassList) throws IOException {
        final Path source = FileSystems.getDefault().getPath(sourcePath);
        final Path destination = FileSystems.getDefault().getPath(destPath);
        Files.walkFileTree(source, new SimpleFileVisitor<Path>(){

            @Override
            public FileVisitResult preVisitDirectory(Path preVisitDirectoryPath,
                                                     BasicFileAttributes attrs) throws IOException {
                Path relativePath = source.relativize(preVisitDirectoryPath);
                String destPathStr = destination +File.separator +relativePath;
                Path destPath = FileSystems.getDefault().getPath(destPathStr);
                boolean pathExists =
                        Files.exists(destPath,
                                new LinkOption[]{ LinkOption.NOFOLLOW_LINKS});
                if (!pathExists){
                    Files.createDirectory(destPath);
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file,
                                             BasicFileAttributes attrs) throws IOException {
                Path relativePath = source.relativize(file);
                String destPathStr = destination +File.separator +relativePath;
                Path destPath = FileSystems.getDefault().getPath(destPathStr);

                if (null != putToMainDexClassList && !putToMainDexClassList.isEmpty()){
                    for (String classPath : putToMainDexClassList){
                        String putMainDexSmaliPath = sourcePath + File.separator + classPath + ".smali";
                        String appPutMainDexSmaliPath = appFirstSmaliPath + File.separator + classPath + ".smali";
                        if(relativePath.toString().equals(classPath + ".smali")){
                            FileUtils.createFile(appPutMainDexSmaliPath, false);
                            FileUtils.copy(putMainDexSmaliPath, appPutMainDexSmaliPath, true);
                            return FileVisitResult.CONTINUE;
                        }
                    }
                }

                boolean isExists = false;
                if (null != filePathList){
                    for (String filePath : filePathList){
                        String destFilePath = filePath.toString() +
                                File.separator + relativePath.toString();
                        if (FileUtils.exists(destFilePath)){
                            isExists = true;
                            Files.copy(file, FileSystems.getDefault().getPath(destFilePath),
                                    StandardCopyOption.REPLACE_EXISTING);
                            break;
                        }
                    }
                }
                if (!isExists){
                    Files.copy(file, destPath, StandardCopyOption.REPLACE_EXISTING);
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc)
                    throws IOException {
                if (null != exc){
                    exc.printStackTrace();
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir,
                                                      IOException exc) throws IOException {
                return FileVisitResult.CONTINUE;
            }

        });
    }

    /**
     * file copy
     *
     * @param srcPath
     * @param destPath
     * @param isReplaceExisting 如果文件存在是否替换
     * @throws IOException
     */
    public static void copy(final String srcPath,final String destPath, final boolean isReplaceExisting) throws IOException {
        final Path source = FileSystems.getDefault().getPath(srcPath);
        final Path destination = FileSystems.getDefault().getPath(destPath);
        Files.walkFileTree(source, new SimpleFileVisitor<Path>(){

            @Override
            public FileVisitResult preVisitDirectory(Path srcdir,
                                                     BasicFileAttributes attrs) throws IOException {
                Path relativizePath = source.relativize(srcdir);
                String destPathStr = destination +File.separator +relativizePath;
                Path destPath = FileSystems.getDefault().getPath(destPathStr);
                boolean pathExists =
                        Files.exists(destPath,
                                new LinkOption[]{ LinkOption.NOFOLLOW_LINKS});
                if (!pathExists){
                    Files.createDirectory(destPath);
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file,
                                             BasicFileAttributes attrs) throws IOException {
                Path relativizePath = source.relativize(file);
                String destPathStr = destination +File.separator +relativizePath;
                Path destPath = FileSystems.getDefault().getPath(destPathStr);
                boolean destPathExists = Files.exists(destPath, LinkOption.NOFOLLOW_LINKS);
                if (destPathExists && !isReplaceExisting){
                    return FileVisitResult.CONTINUE;
                }
                Files.copy(file, destPath, StandardCopyOption.REPLACE_EXISTING);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc)
                    throws IOException {
                if (null != exc){
                    exc.printStackTrace();
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir,
                                                      IOException exc) throws IOException {
                return FileVisitResult.CONTINUE;
            }

        });
    }

    /**
     * get special type file list
     *
     * @param srcPath path
     * @param fileType file type
     * @return file path list
     * @throws IOException
     */
    public static List<String> getFileList(String srcPath, final String fileType) throws IOException {
        final Path source = FileSystems.getDefault().getPath(srcPath);
        final List<String> fileList = new ArrayList<String>();
        Files.walkFileTree(source, new SimpleFileVisitor<Path>(){

            @Override
            public FileVisitResult preVisitDirectory(Path srcdir,
                                                     BasicFileAttributes attrs) throws IOException {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file,
                                             BasicFileAttributes attrs) throws IOException {
                if (file.toFile().getName().endsWith(fileType)){
                    fileList.add(file.toAbsolutePath().toString());
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc)
                    throws IOException {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir,
                                                      IOException exc) throws IOException {
                return FileVisitResult.CONTINUE;
            }

        });
        return fileList;
    }

    /**
     * file copy
     *
     * @param srcPath source path
     * @param destPath destination path
     * @param copyFileType need to copy file type. example .txt or .png
     * @param isReplaceExisting Whether replace if exist.
     * @throws IOException
     */
    public static void copy(final String srcPath, final String destPath,
                            final String copyFileType, final  boolean isReplaceExisting) throws IOException {
        final Path source = FileSystems.getDefault().getPath(srcPath);
        final Path destination = FileSystems.getDefault().getPath(destPath);
        Files.walkFileTree(source, new SimpleFileVisitor<Path>(){

            @Override
            public FileVisitResult preVisitDirectory(Path srcdir,
                                                     BasicFileAttributes attrs) throws IOException {
                Path relativizePath = source.relativize(srcdir);
                String destPathStr = destination +File.separator +relativizePath;
                Path destPath = FileSystems.getDefault().getPath(destPathStr);
                boolean pathExists =
                        Files.exists(destPath,
                                new LinkOption[]{ LinkOption.NOFOLLOW_LINKS});
                if (!pathExists){
                    Files.copy(srcdir, destPath, StandardCopyOption.COPY_ATTRIBUTES);
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file,
                                             BasicFileAttributes attrs) throws IOException {
                Path relativizePath = source.relativize(file);
                String destPathStr = destination +File.separator +relativizePath;
                Path destPath = FileSystems.getDefault().getPath(destPathStr);
                boolean destPathExists = Files.exists(destPath, LinkOption.NOFOLLOW_LINKS);
                if (destPathExists && !isReplaceExisting){
                    return FileVisitResult.CONTINUE;
                }
                if (file.toFile().getName().endsWith(copyFileType)){
                    Files.copy(file, destPath, StandardCopyOption.REPLACE_EXISTING);
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc)
                    throws IOException {
                if (null != exc){
                    exc.printStackTrace();
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir,
                                                      IOException exc) throws IOException {
                return FileVisitResult.CONTINUE;
            }

        });
    }

    /**
     * file move
     *
     * @param srcPath
     * @param destPath
     * @throws IOException
     */
    public static void move(String srcPath, String destPath, final boolean isReplaceExisting) throws IOException {
        final Path source = FileSystems.getDefault().getPath(srcPath);
        final Path destination = FileSystems.getDefault().getPath(destPath);
        Files.walkFileTree(source, new SimpleFileVisitor<Path>(){

            @Override
            public FileVisitResult preVisitDirectory(Path srcdir,
                                                     BasicFileAttributes attrs) throws IOException {
                Path relativizePath = source.relativize(srcdir);
                String destPathStr = destination +File.separator +relativizePath;
                Path destPath = FileSystems.getDefault().getPath(destPathStr);
                boolean pathExists =
                        Files.exists(destPath,
                                new LinkOption[]{ LinkOption.NOFOLLOW_LINKS});
                if (!pathExists){
                    Files.copy(srcdir, destPath, StandardCopyOption.COPY_ATTRIBUTES);
                }else if(isReplaceExisting){
                    Files.copy(srcdir, destPath, StandardCopyOption.REPLACE_EXISTING);
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file,
                                             BasicFileAttributes attrs) throws IOException {
                Path relativizePath = source.relativize(file);
                String destPathStr = destination +File.separator +relativizePath;
                Path destPath = FileSystems.getDefault().getPath(destPathStr);
                Files.move(file, destPath, StandardCopyOption.REPLACE_EXISTING);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc)
                    throws IOException {
                if (null != exc){
                    exc.printStackTrace();
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir,
                                                      IOException exc) throws IOException {
                Files.deleteIfExists(dir);
                return FileVisitResult.CONTINUE;
            }

        });
    }

    /**
     * Copy
     *
     * @param srcPath
     * @param destPath
     * @param isRenameExisting
     * @throws IOException
     */
    public static void copy(final String srcPath,final String destPath,
                            final boolean isRenameExisting, final String prefix) throws IOException {
        final Path source = FileSystems.getDefault().getPath(srcPath);
        final Path destination = FileSystems.getDefault().getPath(destPath);
        Files.walkFileTree(source, new SimpleFileVisitor<Path>(){

            @Override
            public FileVisitResult preVisitDirectory(Path srcdir,
                                                     BasicFileAttributes attrs) throws IOException {
                Path relativizePath = source.relativize(srcdir);
                String destPathStr = destination +File.separator +relativizePath;
                Path destPath = FileSystems.getDefault().getPath(destPathStr);
                boolean pathExists =
                        Files.exists(destPath,
                                new LinkOption[]{ LinkOption.NOFOLLOW_LINKS});
                if (!pathExists){
                    Files.createDirectory(destPath);
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file,
                                             BasicFileAttributes attrs) throws IOException {
                Path relativizePath = source.relativize(file);
                String destPathStr = destination +File.separator +relativizePath;
                Path destPath = FileSystems.getDefault().getPath(destPathStr);
                boolean destPathExists = Files.exists(destPath, LinkOption.NOFOLLOW_LINKS);
                if (destPathExists && isRenameExisting){
                    String fileName = destPath.toFile().getName();
                    String destPathParentPathStr = destPath.toFile().getParentFile().getAbsolutePath();
                    String newFileName = prefix + fileName;
                    String newFilePathStr = destPathParentPathStr + File.separator + newFileName;
                    while (FileUtils.exists(newFilePathStr)){
                        newFileName = prefix + newFileName;
                        newFilePathStr = destPathParentPathStr + File.separator + newFileName;
                    }
                    Files.copy(file, FileSystems.getDefault().getPath(newFilePathStr),
                            StandardCopyOption.COPY_ATTRIBUTES);
                }else{
                    Files.copy(file, destPath, StandardCopyOption.REPLACE_EXISTING);
                }

                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc)
                    throws IOException {
                if (null != exc){
                    exc.printStackTrace();
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir,
                                                      IOException exc) throws IOException {
                return FileVisitResult.CONTINUE;
            }

        });
    }

    /**
     * delete file.
     * path can be directory or file
     *
     * @param path
     * @throws IOException
     */
    public static void delete(String path) throws IOException {
        Path source = FileSystems.getDefault().getPath(path);
        Files.walkFileTree(source, new SimpleFileVisitor<Path>(){

            @Override
            public FileVisitResult preVisitDirectory(Path dir,
                                                     BasicFileAttributes attrs)
                    throws IOException {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file,
                                             BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc)
                    throws IOException {
                if (null != exc){
                    exc.printStackTrace();
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir,
                                                      IOException exc) throws IOException {
                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }

        });
        Files.deleteIfExists(source);
    }

    /**
     * delete child file.
     *
     * @param path
     * @throws IOException
     */
    public static void deleteChildFile(String path) throws IOException {
        File file = new File(path);
        File[] files = file.listFiles();
        for (File childFile : files){
            if (childFile.isDirectory()){
                delete(path);
            }else{
                Files.delete(FileSystems.getDefault().getPath(childFile.getAbsolutePath()));
            }
        }
    }

    /**
     * delete file.
     * path can be directory or file
     *
     * @param path
     * @throws IOException
     */
    public static void deleteIfExists(String path) throws IOException {
        Path source = FileSystems.getDefault().getPath(path);
        boolean pathExists = Files.exists(source,
                new LinkOption[]{ LinkOption.NOFOLLOW_LINKS});
        if (!pathExists){
            return;
        }
        Files.walkFileTree(source, new SimpleFileVisitor<Path>(){

            @Override
            public FileVisitResult preVisitDirectory(Path dir,
                                                     BasicFileAttributes attrs)
                    throws IOException {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file,
                                             BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc)
                    throws IOException {
                if (null != exc){
                    exc.printStackTrace();
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir,
                                                      IOException exc) throws IOException {
                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }
        });

        Files.deleteIfExists(source);
    }

    /**
     * whether file exists
     *
     * @param path
     * @return
     */
    public static boolean exists(String path){
        Path destPath = FileSystems.getDefault().getPath(path);
        return Files.exists(destPath,
                new LinkOption[]{ LinkOption.NOFOLLOW_LINKS});
    }

    /**
     * Create Directories
     *
     * @param path
     * @param isDeleteExists whether delete exits.
     * @throws IOException
     */
    public static void createDirectories(String path, boolean isDeleteExists) throws IOException {
        Path destPath = FileSystems.getDefault().getPath(path);
        boolean pathExists =
                Files.exists(destPath,
                        new LinkOption[]{ LinkOption.NOFOLLOW_LINKS});
        if (pathExists){
            if (isDeleteExists){
                delete(path);
            }else{
                return;
            }
        }
        Files.createDirectories(destPath);
    }

    public static List<String> getAllFilesRelativePath(String filePath){
        final List<String> relativizePathList = new ArrayList<String>();
        try {
            final Path sourceFilePath = FileSystems.getDefault().getPath(filePath);
            Files.walkFileTree(sourceFilePath, new SimpleFileVisitor<Path>(){

                @Override
                public FileVisitResult visitFile(Path file,
                                                 BasicFileAttributes attrs) throws IOException {
                    Path relativizePath = sourceFilePath.relativize(file);
                    relativizePathList.add(relativizePath.toString());
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc)
                        throws IOException {
                    return FileVisitResult.CONTINUE;
                }

            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        return relativizePathList;
    }

    /**
     * Create directory
     *
     * @param path path
     * @param isDeleteExists whether to delete if file exists.
     * @throws IOException
     */
    public static void createDirectory(String path, boolean isDeleteExists) throws IOException {
        Path destPath = FileSystems.getDefault().getPath(path);
        if (isDeleteExists){
            boolean pathExists =
                    Files.exists(destPath,
                            new LinkOption[]{ LinkOption.NOFOLLOW_LINKS});
            if (pathExists){
                delete(path);
            }
        }

        Files.createDirectory(destPath);
    }

    /**
     * Create directory
     *
     * @param path path
     * @throws IOException
     */
    public static void createDirectory(String path) throws IOException {
        Path destPath = FileSystems.getDefault().getPath(path);
        Files.createDirectory(destPath);
    }

    /**
     * Create directories
     *
     * @param path path
     * @throws IOException
     */
    public static void createDirectories(String path) throws IOException {
        Path destPath = FileSystems.getDefault().getPath(path);
        Files.createDirectories(destPath);
    }

    /**
     * create directory if non exists
     *
     * @param path
     * @throws IOException
     */
    public static void createDirectoriesIfNonExists(String path) throws IOException {
        Path destPath = FileSystems.getDefault().getPath(path);
        boolean pathExists =
                Files.exists(destPath,
                        new LinkOption[]{ LinkOption.NOFOLLOW_LINKS});
        if (pathExists){
            return;
        }
        Files.createDirectories(destPath);
    }

    /**
     * create directory if non exists
     *
     * @param path
     * @throws IOException
     */
    public static void createDirectoryIfNonExists(String path) throws IOException {
        Path destPath = FileSystems.getDefault().getPath(path);
        boolean pathExists =
                Files.exists(destPath,
                        new LinkOption[]{ LinkOption.NOFOLLOW_LINKS});
        if (pathExists){
            return;
        }
        Files.createDirectory(destPath);
    }

    /**
     * create file
     *
     * @param path file path
     * @param isDeleteIfExists Whether to delete file if exists
     * @throws IOException
     */
    public static void createFile(String path, boolean isDeleteIfExists) throws IOException {
        Path filePath =
                FileSystems.getDefault().getPath(path);
        boolean filePathExists = Files.exists(filePath,
                new LinkOption[]{LinkOption.NOFOLLOW_LINKS});
        if (filePathExists){
            if (isDeleteIfExists){
                delete(path);
            }else{
                return;
            }
        }
        File f = new File(path);
        FileUtils.createDirectoriesIfNonExists(f.getParentFile().getAbsolutePath());
        Files.createFile(filePath);
    }


}
