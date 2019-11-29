package io.cex.test.framework.assertutil;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.cex.test.framework.dbutil.DataBaseManager;
import io.cex.test.framework.jsonutil.JsonFileUtil;
import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;
import java.util.*;

import static org.testng.internal.EclipseInterface.*;

/**
 * @author shenqingyan
 * @create 2018/4/17 20:30
 * @desc assert different json
 **/
@Slf4j
public class AssertTool extends Assert{
    /**
     * @desc 比较两个JSONObject是否完全相等的方法
     * @param expect 预期值
     * @param actual 实际值
     **/

    private static boolean totallyEqual(JSONObject expect, JSONObject actual){
        if(null == expect) {
            fail("Expected a null object, but not null found. ");
        }
        if(null == actual) {
            fail("Expected not null object, but null found. ");
        }
        return expect.equals(actual);
    }

    /**
     * @desc 比较两个JSONObject是否完全相等的方断言
     * @param expect 预期值
     * @param actual 实际值
     **/
    public static void isTotallyEqual(JSONObject expect, JSONObject actual){
        if (AssertTool.totallyEqual(expect,actual)){
            log.info("------Assert true：two JSONObject is totally equal");
        }else {
            fail(format(actual, expect, "------Assert failed：two JSONObject is totally equal"));
        }
    }


    /**
     * @desc 判断实际json 包含期望json值方法
     * @param expect 预期值
     * @param actual 实际值
     **/
    private static boolean containsExpect(JSONObject expect, JSONObject actual){
        HashMap<String, Object> exmap = new HashMap<String, Object>();
        HashMap<String, Object> acmap = new HashMap<String, Object>();
        HashMap<String, Object> tmp = new HashMap<String, Object>();
        //获取json中所有的key-value
        exmap = JsonFileUtil.jsonToMap(expect,exmap);
        acmap = JsonFileUtil.jsonToMap(actual, acmap);
        int sum = 0;
        //对比预期及实际值中json的key-value
        for (String key: exmap.keySet()){
            if (acmap.keySet().contains(key) && exmap.get(key).equals(acmap.get(key))){
                sum += 1;
            }
        }
        if (sum == exmap.size()){
            return true;
        }else {
            return false;
        }
    }

    /**
     * @desc 判断实际json 包含期望json值断言
     * @param expect 预期值
     * @param actual 实际值
     **/
    public static void isContainsExpect(JSONObject expect, JSONObject actual){
        if(null == expect) {
            fail("----Assert failed:Expected a null JsonObject, but not null found. " );
        }
        if(null == actual) {
            fail("----Assert failed:Expected not null JsonObject, but null found. ");
        }
        //如果包含，返回true
        if (AssertTool.containsExpect(expect,actual)){
            log.info("----Assert true: Actual json contains expect json");
        }else {
            //不包含，断言失败
            fail(format(actual, expect, "----Assert failed: Actual json not contains expect json"));
        }
    }

    /**
     * @desc 判断实际JSONArray 是否包含 某个json方法
     * @param expect 期望值
     * @param actual 实际值
     **/
    private static boolean containsExpect(JSONObject expect, JSONArray actual){
        boolean result = false;
        if (actual.size() > 0){
            for (int i = 0; i< actual.size(); i++){
                JSONObject acobj = actual.getJSONObject(i);
                if (containsExpect(expect, acobj)){
                    result = true;
                    return result;
                }
            }
        }
        return result;
    }

    /**
     * @desc 判断实际JSONArray 是否包含 某个json断言
     * @param expect 期望值
     * @param actual 实际值
     **/
    public static void isContainsExpect(JSONObject expect, JSONArray actual){
        if(null == expect) {
            fail("----Assert failed:Expected a null JsonObject, but not null found. ");
        }
        if(null == actual || actual.size() == 0) {
            fail("----Assert failed:Expected not null JsonObject, but null found. ");
        }
        if (AssertTool.containsExpect(expect,actual)){
            log.info("----Assert true: Actual contains Expected");
        }else {
            fail(format(actual, expect, "----Assert failed:Actual not contains Expected"));
        }
    }


    /**
     * @desc 判断实际json 包含期望json NODE节点的键值对方法
     * @param expect 期望值
     * @param actual 实际值
     **/
    private static boolean containsExpectJsonNode(JSONObject expect, JSONObject actual){
        HashMap<String, Object> exmap = new HashMap<String, Object>();
        HashMap<String, Object> acmap = new HashMap<String, Object>();
        exmap = JsonFileUtil.jsonNodeToMap(expect,exmap);
        acmap = JsonFileUtil.jsonNodeToMap(actual, acmap);
        int sum = 0;
        for (String key: exmap.keySet()){
            if (acmap.keySet().contains(key) && exmap.get(key).equals(acmap.get(key))){
                sum += 1;
            }
        }
        if (sum == exmap.size()){
            return true;
        }else {
            return false;
        }
    }

    /**
     * @desc 判断实际json 包含期望json NODE节点的键值对断言
     * @param expect 期望值
     * @param actual 实际值
     **/
    public static void isContainsExpectJsonNode(JSONObject expect, JSONObject actual){
        if(null == expect) {
            fail("----Assert failed:expected a null JsonObject, but not null found. ");
        }
        if(null == actual) {
            fail("----Assert failed:expected not null JsonObject, but null found. " );
        }
        if (AssertTool.containsExpectJsonNode(expect,actual)){
            log.info("----Assert true:Actual contains the key-value of Expected");
        }else {
            fail(format(actual, expect, "----Assert failed:Actual not contains the key-value of Expected"));
        }
    }

    /**
     * @desc 判断实际jsonarray 包含期望json NODE节点的键值对方法
     * @param expect 期望值
     * @param actual 实际值
     **/
    private static boolean containsExpectJsonNode(JSONObject expect, JSONArray actual){
        boolean result = false;
        if (actual.size() > 0){
            for (int i = 0; i< actual.size(); i++){
                JSONObject acobj = actual.getJSONObject(i);
                if (containsExpectJsonNode(expect, acobj)){
                    result = true;
                    return result;
                }
            }
        }else {
            log.info("jsonarray为空");
        }
        return result;
    }

    /**
     * @desc 判断实际jsonarray 包含期望json NODE节点的键值对断言
     * @param expect 期望值
     * @param actual 预期值
     **/
    public static void isContainsExpectJsonNode(JSONObject expect, JSONArray actual){
        if(null == expect) {
            fail("----Assert failed:expected a null JsonObject, but not null found. ");
        }
        if(null == actual || actual.size() == 0) {
            fail("----Assert failed:expected not null JsonObject, but null found. ");
        }
        if (AssertTool.containsExpectJsonNode(expect,actual)){
            log.info("----Assert true:Actual JSONArray contains expect json value");
        }else {
            fail(format(actual, expect, "----Assert failed:Actual JSONArray not contains expect json value"));
        }
    }

    /**
     * @desc 判断实际数据库 包含期望json NODE节点的键值对断言
     * @param expect 期望值
     * @param dburl 数据库连接信息
     * @param sql 执行sql
     **/
    private static boolean containsExpectJsonNode(String expect, String dburl, String sql){
        DataBaseManager dataBaseManager = new DataBaseManager();
        JSONArray array = dataBaseManager.executeSingleQuery(sql,dburl);
        if(array.size() == 0) {
            fail("----Assert failed:expected not null JsonObject, but null found. ");
        }
        for (int i = 0; i<array.size();i++){
            log.info(array.get(i).toString());
        }
        JSONObject expjson = JSON.parseObject(expect);
        return containsExpectJsonNode(expjson,array);
    }
    /**
     * @desc 判断数据库结果是否包含预期json子键值对断言
     * @param expect 期望值
     * @param dburl 数据库连接信息
     * @param sql 执行sql
     **/
    public static void isContainsExpectJsonNode(String expect, String dburl, String sql){

        if (AssertTool.containsExpectJsonNode(expect,dburl,sql)){
            log.info("----Assert true:SQL result contains key-value of expect");
        }else {
            fail(format(" ", expect, "assert failed"));
        }
    }
    /**
     * @desc 判断实际string包含期望string值方法
     * @param expect 期望值
     * @param actual 实际值
     **/
    private static boolean containsExpect(String expect, String actual){
        return actual.contains(expect);
    }

    /**
     * @desc 判断实际string包含期望string值断言
     * @param expect 期望值
     * @param actual 实际值
     **/
    public static void isContainsExpect(String expect, String actual){
        if(null == expect) {
            fail("----Assert failed:expected a null JsonObject, but not null found. ");
        }
        if(null == actual) {
            fail("----Assert failed:expected not null JsonObject, but null found. ");
        }
        if (AssertTool.containsExpect(expect,actual)){
            log.info("----Assert true:Actual string contains expect string");
        }else {
            fail(format(actual, expect, "----Assert failed:Actual string not contains expect string"));
        }
    }

    /**
     * @desc 判断数据库结果是否包含预期json方法
     * @param expect 期望值
     * @param dburl 数据库连接信息
     * @param sql 执行SQL
     **/
    private static boolean containsExpect(String expect, String dburl, String sql){
        DataBaseManager dataBaseManager = new DataBaseManager();
        JSONArray array = dataBaseManager.executeSingleQuery(sql,dburl);
        if(array == null || array.size() == 0) {
            fail("----Assert failed:expected not null JsonObject, but null found. ");
        }
        for (int i = 0; i<array.size();i++){
            log.info(array.get(i).toString());
        }
        JSONObject expjson = JSON.parseObject(expect);
        return containsExpect(expjson,array);
    }


    /**
     * @desc 判断数据库结果是否包含预期json断言
     * @param expect 期望值
     * @param dburl 数据库连接信息
     * @param sql 执行SQL
     **/
    public static void isContainsExpect(String expect, String dburl, String sql){

        if (AssertTool.containsExpect(expect,dburl,sql)){
            log.info("----Assert true:SQL result contains expect json");
        }else {
            fail(format(" ", expect, "----Assert failed:SQL result not contains expect json"));
        }
    }
    /**
     * @desc 判断实际json不包含期望json方法
     * @param expect 期望值
     * @param actual 实际值
     **/
    private static boolean notContainsExpect(JSONObject expect, JSONObject actual) {
        HashMap<String, Object> exmap = new HashMap<String, Object>();
        HashMap<String, Object> acmap = new HashMap<String, Object>();
        exmap = JsonFileUtil.jsonToMap(expect, exmap);
        acmap = JsonFileUtil.jsonToMap(actual, acmap);
        int sum = 0;
        for (String key : exmap.keySet()) {
            if (acmap.keySet().contains(key) && exmap.get(key).equals(acmap.get(key))) {
                sum += 1;
            }
        }
        if (sum != exmap.size()) {
            return true;
        } else {
            return false;
        }
    }
    /**
     * @desc 判断实际jsonnode不包含期望json方法
     * @param expect 期望值
     * @param actual 实际值
     **/
    private static boolean notContainsExpectNode(JSONObject expect, JSONObject actual) {
        HashMap<String, Object> exmap = new HashMap<>();
        HashMap<String, Object> acmap = new HashMap<>();
        exmap = JsonFileUtil.jsonNodeToMap(expect, exmap);
        acmap = JsonFileUtil.jsonNodeToMap(actual, acmap);
        int sum = 0;
        for (String key : exmap.keySet()) {
            if (acmap.keySet().contains(key) && exmap.get(key).equals(acmap.get(key))) {
                sum =sum + 1;
            }
        }
        if (sum != exmap.size()) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * @desc 判断实际json不包含期望json断言
     * @param expect 期望值
     * @param actual 实际值
     **/
    public static void isNotContainsExpect(JSONObject expect, JSONObject actual){
        if(null == expect) {
            fail("----Assert failed:expected is a null JsonObject ");
        }
        if(null == actual) {
            fail("----Assert failed:actual is a null JsonObject");
        }
        if (AssertTool.notContainsExpect(expect,actual)){
            log.info("----Assert true:Actual json not contains expect json");
        }else {
            failSame(actual,expect,"----Assert failed:Actual json contains expect json");
        }
    }

    /**
     * @desc 判断实际json不包含期望jsonNODE断言
     * @param expect 期望值
     * @param actual 实际值
     **/
    public static void isNotContainsExpectJsonNode(JSONObject expect, JSONObject actual){
        if(null == expect) {
            fail("----Assert failed:expected is a null JsonObject ");
        }
        if(null == actual) {
            fail("----Assert failed:actual is a null JsonObject");
        }
        if (AssertTool.notContainsExpectNode(expect,actual)){
            log.info("----Assert true:Actual json not contains key-value of expect");
        }else {
            failSame(actual,expect,"----Assert failed:Actual json contains key-value of expect");
        }
    }

    /**
     * @desc 判断实际jsonarry不包含期望json方法
     * @param expect 期望值
     * @param actual 实际值
     **/
    private static boolean notContainsExpect(JSONObject expect, JSONArray actual){
        int sum = 0;
        if (actual.size()> 0){
            for (int i = 0; i< actual.size();i++){
                JSONObject object = actual.getJSONObject(i);
                if (notContainsExpect(expect, object)){
                    sum +=1;
                }
            }
        }
        if (sum == actual.size()){
            return true;
        }else {
            return false;
        }

    }

    /**
     * @desc 判断实际jsonarry不包含期望jsonNODE方法
     * @param expect 期望值
     * @param actual 实际值
     **/
    private static boolean notContainsExpectNode(JSONObject expect, JSONArray actual){
        int sum = 0;
        if (actual.size()> 0){
            for (int i = 0; i< actual.size();i++){
                JSONObject object = actual.getJSONObject(i);
                if (notContainsExpectNode(expect, object)){
                    sum +=1;
                }
            }
        }
        if (sum == actual.size()){
            return true;
        }else {
            return false;
        }
    }

    /**
     * @desc 判断实际jsonarry不包含期望json
     * @param expect 期望值
     * @param actual 实际值
     **/
    public static void isNotContainsExpect(JSONObject expect, JSONArray actual){
        if(null == expect) {
            fail("----Assert failed:expected is a null JsonObject ");
        }
        if(null == actual) {
            fail("----Assert failed:actual is a null JsonObject");
        }
        if (AssertTool.notContainsExpect(expect,actual)){
            log.info("----Assert true:Actual jsonArray not contains expect json");
        }else {
            failSame(actual,expect,"----Assert failed:Actual jsonArray contains expect json");
        }
    }


    /**
     * @desc 判断实际jsonarry不包含期望json
     * @param expect 期望值
     * @param actual 实际值
     **/
    public static void isNotContainsExpectJsonNode(JSONObject expect, JSONArray actual){
        if(null == expect) {
            fail("----Assert failed:expected is a null JsonObject ");
        }
        if(null == actual || actual.size() == 0) {
            fail("----Assert failed:actual is a null JsonObject");
        }
        if (AssertTool.notContainsExpectNode(expect,actual)){
            log.info("----Assert true:Actual jsonArray not contains key-value of expect");
        }else {
            failSame(actual,expect,"----Assert failed:Actual jsonArray contains key-value of expect");
        }
    }

    /**
     * @desc 判断数据库中是否不包含预期json方法
     * @param expect 期望值
     * @param dburl 数据库连接信息
     * @param sql 执行SQL语句
     **/
    private static boolean notContainsExpect(String expect,String dburl, String sql){
        DataBaseManager dataBaseManager = new DataBaseManager();
        JSONArray array = dataBaseManager.executeSingleQuery(sql,dburl);
        if(null == array || array.size() == 0) {
            fail("----Assert failed:actual is a null JsonObject");
        }
        for (int i = 0; i<array.size();i++){
            log.info(array.get(i).toString());
        }
        JSONObject expjson = JSON.parseObject(expect);
        return notContainsExpect(expjson, array);
    }

    /**
     * @desc 判断数据库中是否不包含预期jsonnode方法
     * @param expect 期望值
     * @param dburl 数据库连接信息
     * @param sql 执行SQL语句
     **/
    private static boolean notContainsExpectNode(String expect,String dburl, String sql){
        DataBaseManager dataBaseManager = new DataBaseManager();
        JSONArray array = dataBaseManager.executeSingleQuery(sql,dburl);
        if(null == array || array.size() == 0) {
            fail("----Assert failed: actual is a null JsonObject");
        }
        for (int i = 0; i<array.size();i++){
            log.info(array.get(i).toString());
        }
        JSONObject expjson = JSON.parseObject(expect);
        return notContainsExpectNode(expjson, array);
    }

    /**
     * @desc 判断数据库中是否不包含预期json断言
     * @param expect 期望值
     * @param dburl 数据库连接信息
     * @param sql 执行SQL语句
     **/
    public static void isNotContainsExpect(String expect,String dburl, String sql){

        if (AssertTool.notContainsExpect(expect,dburl,sql)){
            log.info("----Assert true:SQL result not contains expect json");
        }else {
            fail("----Assert failed: SQL result contains expect json");
        }
    }

    /**
     * @desc 判断数据库中是否不包含预期jsonnode断言
     * @param expect 期望值
     * @param dburl 数据库连接信息
     * @param sql 执行SQL语句
     **/
    public static void isNotContainsExpectJsonNode(String expect,String dburl, String sql){
        if (AssertTool.notContainsExpectNode(expect,dburl,sql)){
            log.info("----Assert true:SQL result not contains key-value of expect json");
        }else {
            fail("----Assert failed:SQL result contains key-value of expect json");
        }
    }

    /**
     * @desc 判断实际值比预期值大的断言
     * @param actual 实际输入的值,该值为String型
     * @param except 预期的值
     */
    public static void isGreaterThan(String actual,int except){
        if (actual==null||actual.equals("")){
            fail("----Assert failed：actual is null or actual is empty----");
        }
        try {
            int actualReal =Integer.parseInt(actual);
            if (actualReal>except){
                log.info("------Assert true：acutal is greater than except");
            }else {
                fail(format(actual,except,"----Assert failed：Actual number not greater than expect number"));
            }
        }catch (NumberFormatException e){
            fail("----Assert failed：NumberFormatException，please input  String than can transform into number----");
        }


    }

    /**
     * @desc 判断实际值比预期值大的断言
     * @param actual 实际输入的值,该值为Int型
     * @param except 预期的值
     */
    public static void isGreaterThan(int actual,int except){
        if (actual>except){
            log.info("------Assert true：acutal is greater than except");
        }else {
            fail(format(actual,except,"----Assert failed：Actual number not greater than expect number"));
        }

    }

    /**
     * @desc 格式化异常信息
     * @param message
     * @param expect
     * @param actual
     **/
    private static String format(Object actual, Object expect, String message) {
        String formatted = "";
        if (null != message) {
            formatted = message + " ";
        }
        return formatted + ASSERT_LEFT + expect + ASSERT_MIDDLE + actual + ASSERT_RIGHT;
    }

    private static void failSame(Object actual, Object expected, String message) {
        String formatted = "";
        if(message != null) {
            formatted = message + " ";
        }
        fail(formatted + ASSERT_LEFT2 + expected + ASSERT_MIDDLE + actual + ASSERT_RIGHT);
    }
}

