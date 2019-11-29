package io.cex.test.framework.common;


import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

import java.security.MessageDigest;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;


/**
 * @author shenqingyan
 * @create 2018/4/5 17:01
 * @desc
 **/
@Slf4j
public class CheckSignUtil {
    /**
    * @desc 获取字符串的MD5哈希值
    * @param  md5Str 字符串
    **/
    public static String getMD5(String md5Str) {

        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(md5Str.getBytes("utf-8"));
            //byte[] bytes = md5.digest(md5Str.getBytes());
            StringBuffer sb = new StringBuffer();
            String temp = "";
            for (byte b : bytes) {
                temp = Integer.toHexString(b & 0XFF);
                sb.append(temp.length() == 1 ? "0" + temp : temp);
            }
            return sb.toString();
        } catch (Exception e) {
            log.error("getMD5 with unexpected exception, str:{}", md5Str, e);
            return null;
        }
    }

    /**
    * @desc 生成加密sign串
    * @param  map 验签参数
    **/

    public static String generateSignByRule(TreeMap<String, String> map) {
        String sign = "";
        Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
            if (it.hasNext()) {
                if (!(entry.getValue().toString() == null || (entry.getValue().toString()).equals(""))) {
                    sign += entry.getKey() + "=" + entry.getValue() + "&";
                }
            }
            else {
                if (!(entry.getValue().toString() == null || entry.getValue().toString().equals(""))) {
                    sign += entry.getKey() + "=" + entry.getValue();
                }
                else {
                    sign = sign.substring(0, sign.length() - 1);
                }
            }
        }

        return getMD5(sign);
    }

    /**
    * @desc 把需要验签的参数封装到TreeMap中,生成加密sign串
    * @param
    **/
    public static String generateSign(JSONObject jsonObject, String secretKey) throws Exception {
        jsonObject.put("secretkey", secretKey);
        TreeMap<String, String> treeMap = new TreeMap<>();
        getTreeMapFromJSONObject(jsonObject, treeMap);
        treeMap.remove("sign");
        String generateSign = CheckSignUtil.generateSignByRule(treeMap);
        return generateSign;
    }

    /**
    * @desc JSONObject 生成tree map
    * @param
    **/
    private static void getTreeMapFromJSONObject(JSONObject jsonObject, TreeMap<String, String> treeMap) {
        for (String key : jsonObject.keySet()) {
            if (treeMap.containsKey(key)) {
                log.info("duplicate data field,key:{},treeMap:{},jsonObject:{}", key, treeMap.toString(), jsonObject.toJSONString());
            }
            Object value = jsonObject.get(key);
            if (value instanceof JSONObject) {
                //递归调用
                getTreeMapFromJSONObject((JSONObject) value, treeMap);
            } else if (value != null) {
                treeMap.put(key, value.toString());
            }
        }
    }
}
