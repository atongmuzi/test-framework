package io.cex.test.framework.jsonutil;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.alibaba.fastjson.*;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer;
import io.cex.test.framework.common.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
/**
 * @author shenqingyan
 * @create 2019/7/29 16:45
 * @desc 替换json value为随机值
 **/
@Slf4j
public class JsonReplaceUtil {
    /**
    * @desc 读取json文件
     * @param  path json路径
    **/
    public JSON jsonReader(String path){

        JsonFileRead jsonFileRead = new JsonFileRead();
        String jsonstr = jsonFileRead.txtToString(path);
        JSON json = JSON.parseObject(jsonstr);
        return json;
    }
    /**
    * @desc json内容替换
    * @param  jsonObject 要替换的json
    **/
    public void jsonReplace(JSONObject jsonObject){
        Type type = null;
        for (Map.Entry<String, Object> entry : jsonObject.entrySet()){
            String str = entry.getValue().getClass().getTypeName();
            RandomUtil randomUtil = new RandomUtil();
            if (str == "com.alibaba.fastjson.JSONObject"){
                jsonReplace((JSONObject)entry.getValue());
            }else if (str =="java.lang.String"){

                String regEx = "[\\u4e00-\\u9fa5]";
                Pattern p = Pattern.compile(regEx);
                Matcher matcher = p.matcher(entry.getValue().toString());
                if (StringUtils.isNumeric(entry.getValue().toString())){
                    //字符串是数字，设置随机数
                    entry.setValue(randomUtil.generateLong(entry.getValue().toString().length()));
                }else if (matcher.find()){
                    //字符串包含中文，设置随机中文字符
                    String t = randomUtil.generateStringChinese(entry.getValue().toString().length());
                    String value = new String(t);
                    entry.setValue(value);
                }else if(this.isValidDate(entry.getValue().toString())){
                    //字符串是时间，设置随机时间
                    //  System.out.println(entry.getKey()+":"+"time");
                    entry.setValue(randomUtil.generateTime());
                }else {
                    //其他则设置中英文随机字符串
                    entry.setValue(randomUtil.generateString(entry.getValue().toString().length()));
                }
            }else if(str == "com.alibaba.fastjson.JSONArray"){
                JSONArray ja = (JSONArray)entry.getValue();
                for (Object o : ja){
                    JSONObject jo = (JSONObject)o;
                    jsonReplace(jo);
                }
            }else if(str == "java.lang.Integer"){
                //int类型设置随机int值
                entry.setValue(randomUtil.generateInt(entry.getValue().toString().length()));
            }else if (str == "java.lang.Long"){
                //Long类型设置随机long值
                entry.setValue(randomUtil.generateLong(entry.getValue().toString().length()));
            }


        }

        log.info("-----------replaced json is:"+jsonObject);
    }

    /**
    * @desc 判断字符串是否为有效时间
    * @param  inDate 时间字符串
    **/
    public boolean isValidDate(String inDate) {

        if (inDate == null)
            return false;

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        if (inDate.trim().length() != dateFormat.toPattern().length())
            return false;
        //标志严格解析日期
        dateFormat.setLenient(true);

        try {
            //格式化时间
            dateFormat.parse(inDate.trim());
        }
        catch (ParseException pe) {
            log.error("---------------Time Parse ERROR");
            return false;

        }
        return true;
    }
}
