package io.cex.test.framework.common;


import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

/**
 * @author shenqingyan
 * @create 2018/4/11 10:51
 * @desc 字符串处理工具类
 **/
@Slf4j
public class StringUtil {
    private static String SYSTEM_TYPE = "systemType";

    /**
    * @desc 首字母小写
    * @param  s 字符串
    **/
    public static String firstToLow(String s) {

        String str;
        if (s.length() > 2) {
            str = s.substring(0, 1).toLowerCase() + s.substring(1);
        } else {
            str = s;
        }
        return str;
    }
    /**
     * @desc 首字母大写
     * @param  s 字符串
     **/
    public static String firstToUp(String s) {

        String str;
        if (s.length() > 2) {
            str = s.substring(0, 1).toUpperCase() + s.substring(1);
        } else {
            str = s;
        }
        return str;
    }
    /**
    * @desc 获取properties文件的某个属性值
    * @param  file 文件
     * @param properName 属性
    **/
    public static String getPro(String file, String properName) {
        Properties prop = new Properties();
        String value = null;
        InputStream in = Object.class.getResourceAsStream("/" + file);
        try {
            prop.load(in);
            value = prop.getProperty(properName).trim();
        } catch (IOException e) {
            e.printStackTrace();
            log.error("properties file is not exist");
        } finally {
            return value;
        }
    }

    /**
    * @desc 判断对象是否为空
    * @param  value 对象
    **/
    public static Boolean isEmpty(Object value) {
        if (value == null) return true;
        if (org.apache.commons.lang.StringUtils.isBlank(value.toString())) return true;
        return false;
    }
    /**
    * @desc 判断对象是否为json
    * @param  value 对象
    **/
    public static boolean isJson(Object value) {
        if (!(value instanceof String)) return false;
        String json = value.toString();
        return json.startsWith("{") && json.endsWith("}");
    }
    /**
    * @desc 返回config文件配置信息
    * @param  file 文件
     * @param defaultValue 默认值
     * @param properName 属性
    **/
    public static String getConfig(File file, String properName, String defaultValue) {
        List<String> lines = FileUtil.fileReadeForList(file);
        for (String line : lines) {
            if (!line.startsWith("#")) {
                if (line.contains("=")) {
                    String split[] = line.split("=", 2);
                    if (split[0].equals(properName)) {
                        return split[1];

                    }

                }
            }
        }
        return defaultValue;
    }



    /**
    * @desc 增加xml格式字符串，且光标在新增字符串前
    * @param  result 最后结果
     * @param s 增加的字符串
     * @param before 已有的xml字符串
    **/
    public static void addXMLStartString(StringBuffer result, String s, StringBuffer before) {
        result.append(before).append("<").append(s).append(">");
        addBefore(before);
    }
    /**
    * @desc param前增加字符串
    * @param result 最后结果
     * @param before 新增字符串
    **/
    public static void addParamStartString(StringBuffer result, StringBuffer before) {
        result.append(before).append("{\r\n\t\"param\":\r\n\t[\r\n");
        addBefore(before);
    }
    /**
     * @desc param后增加字符串
     * @param result 最后结果
     * @param before 新增字符串
     **/
    public static void addParamEndString(StringBuffer result, StringBuffer before) {
        result.append(before).append("]\r\n");
        delBefore(before);
        result.append(before).append("}");
    }
    /**
     * @desc json前增加字符串
     * @param result 最后结果
     * @param before 新增字符串
     **/
    public static void addJsonStartList(StringBuffer result, StringBuffer before) {

        result.append("\n").append(before).append("[").append("\n");
        addBefore(before);
    }
    /**
    * @desc 删除最后一个字符
    * @param  before 待处理字符串
    **/
    public static void delBefore(StringBuffer before) {
        before.deleteCharAt(before.length() - 1);

    }
    /**
    * @desc 字符串加\t，把光标移动到字符串之后
    * @param
    **/
    public static void addBefore(StringBuffer before) {
        before.append("\t");

    }

    public static void addJsonStart(StringBuffer result, String fieldName, StringBuffer before) {
        result.append(before).append("\"").append(fieldName).append("\"").append(":");

    }

    public static void addHead(StringBuffer result, StringBuffer before) {

        result.append(before).append("{\n");
        addBefore(before);
    }

    public static void addEnd(StringBuffer result, StringBuffer before) {
        delBefore(before);
        result.append(before).append("},\n");

    }

    public static void addJsonEndList(StringBuffer result, StringBuffer before) {
        delBefore(before);
        result.append(before).append("],").append("\n");

    }

    public static void addJsonStringDefaultValue(StringBuffer result, String value) {
        if (value == null) {
            result.append("\"").append("\",\n");
        } else {
            result.append("\"").append(value).append("\",\n");
        }
    }

    public static void addJsonOtherDefaultValue(StringBuffer result, String value) {
        if (value == null) {
            result.append(",\n");
        } else {
            result.append(value).append(",\n");
        }

    }

    public static void addXMLEndString(StringBuffer result, String s, StringBuffer before, boolean falg) {
        delBefore(before);
        if (falg) {
            result.append(before);
        }
        result.append("</").append(s).append(">\n");


    }


}

