package io.cex.test.framework.jsonutil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * @author shenqingyan
 * @create 2019/7/29 16:43
 * @desc 读取resource目录下json文件
 **/
public class JsonFileRead {
    public String txtToString(String path){
        File file = new File(this.getClass().getResource(path).getPath());
        BufferedReader bufferedReader = null;
        StringBuffer sb = new StringBuffer();
        String tmp = null;
        if (file.isFile() && file.exists()){
            try {
                bufferedReader = new BufferedReader(new FileReader(file));
                while ((tmp = bufferedReader.readLine())!= null){
                    sb.append(tmp);

                }
                return sb.toString();
            }catch (Exception e){
                return e.getMessage();
            }
        }else {
            return "文件不存在";
        }

    }
}
