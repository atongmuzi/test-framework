package io.cex.test.framework.common;


import com.google.common.base.Charsets;
import com.google.common.io.Files;
import lombok.extern.slf4j.Slf4j;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author shenqingyan
 * @create 2016/4/5 10:26
 * @desc 文件操作工具
 **/
@Slf4j
public class FileUtil {
    /**
    * @desc 检查文件夹是否存在
    * @param  folderName 文件路径
    **/
    public static boolean makeDirs(String folderName) {
        File folder = new File(folderName);
        return (folder.exists() && folder.isDirectory()) ? true : folder.mkdirs();
    }
    /**
    * @desc 写文件
    * @param  fileName 文件名
     * @param  contents 文件内容
    **/
    public static void fileWrite(String fileName, String contents) {
        checkNotNull(fileName, "Provided file name for writing must NOT be null.");
        checkNotNull(contents, "Unable to write null contents.");
        int i = fileName.lastIndexOf("/");
        String folder = fileName.substring(0, i);
        makeDirs(folder);
        final File newFile = new File(fileName);
        try {
            Files.write(contents.getBytes(StandardCharsets.UTF_8), newFile);
        } catch (IOException fileIoEx) {
            log.error("ERROR trying to write to file '" + fileName + "' - "
                    + fileIoEx.toString());
        }
    }
    /**
    * @desc 读取文件内容，返回list
    * @param  file 文件
    **/
    public static List fileReadeForList(File file) {

        List<String> lines = null;

        try {
            lines = Files.readLines(file, Charsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }
    /**
    * @desc 读取文件内容，返回string
    * @param  file 文件
    **/
    public static String fileReadeForStr(File file) {

        List<String> lines = null;
        String result = "";
        try {
            lines = Files.readLines(file, Charsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (String line : lines) {
            result += "\t\t"+line + "\n";
        }
        return result;
    }
    /**
    * @desc 删除文件或文件夹
    * @param  path 文件路径
    **/
    public static void deleteAllFilesOfDir(File path) {
        if (!path.exists())
            return;
        if (path.isFile()) {
            path.delete();
            return;
        }
        File[] files = path.listFiles();
        for (int i = 0; i < files.length; i++) {
            deleteAllFilesOfDir(files[i]);
        }
        path.delete();
    }
}
