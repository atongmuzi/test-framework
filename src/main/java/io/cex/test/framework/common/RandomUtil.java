package io.cex.test.framework.common;


import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 * @author shenqingyan
 * @create 2019/3/10 15:27
 * @desc 随机值工具类
 **/
@Slf4j
public class RandomUtil {
    /**
     * @desc 生成定长的int数据
     * @param length 长度
     **/
    public static final String ALLCHAR = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static int generateInt(int length){
        Random random = new Random();
        String str = "";
        int n = 0;
        for ( n = 0; n<length; n++){
            str += random.nextInt(10);
        }
        return Integer.parseInt(str);
    }

    /**
     * @desc 生成定长的LONG数据
     * @param  length 长度
     **/
    public static Long generateLong(int length){
        Random random = new Random();
        String str = "";
        int n = 0;
        for ( n = 0; n<length; n++){
            str += random.nextInt(10);
        }
        return Long.parseLong(str);
    }

    /**
     * @desc 生成定长英文及数字的随机数据
     * @param  length 长度
     **/
    public static String generateString(int length){
        StringBuffer stringBuffer = new StringBuffer();
        Random random = new Random();
        for (int i = 0; i<length;i++){
            stringBuffer.append(ALLCHAR.charAt(random.nextInt(ALLCHAR.length())));
        }
        return stringBuffer.toString();
    }

    /**
     * @desc 生成定长随机中文
     * @param  length 长度
     **/
    public static String generateStringChinese(int length){
        String ret="";
        for(int i=0;i<length;i++){
            String str = null;
            int hightPos, lowPos; // 定义高低位
            Random random = new Random();
            hightPos = (176 + Math.abs(random.nextInt(39))); //获取高位值
            lowPos = (161 + Math.abs(random.nextInt(93))); //获取低位值
            byte[] b = new byte[2];
            b[0] = (new Integer(hightPos).byteValue());
            b[1] = (new Integer(lowPos).byteValue());
            try
            {
                str = new String(b, "GBk"); //转成中文
            }
            catch (UnsupportedEncodingException ex)
            {
                ex.printStackTrace();
                log.error("GenerateStringEncodingError");
            }
            ret+=str;
        }
        return ret;
    }

    /**
     * @desc 生成随机日期
     **/
    public static String generateTime(){
        Random rand = new Random();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Calendar cal = Calendar.getInstance();
        cal.set(2007, 0, 1);
        long start = cal.getTimeInMillis();
        cal.set(2019, 0, 1);
        long end = cal.getTimeInMillis();

        Date d = new Date(start + (long)(rand.nextDouble() * (end - start)));
        //      System.out.println(format.format(d));
        return format.format(d);
    }

    /**
    * @desc 生成范围内随机数字
    * @param start 范围起始值
    * @param end 范围截至值
    **/
    public static int getNum(int start, int end) {
        return (int) (Math.random() * (end - start + 1) + start);
    }

    /**
    * @desc 生成随机电话号码
    **/
    public static String getRandomPhoneNum() {
        String[] telFirst = "134,135,136,137,138,139,150,151,152,157,158,159,130,131,132,155,156,133,153,181,182,183,184".split(",");
        int index = getNum(0, telFirst.length - 1);
        String first = telFirst[index];
        Long second = generateLong(8);
        return first + second;

    }

    /**
    * @desc 生成随机邮箱地址，后缀为@chacuo.net
    **/
    public static String getRandomMail(){
        String first = RandomUtil.generateString(10);
        Long second = generateLong(8);
        String third = "@chacuo.net";
        return first + second + third;
    }
/*
    public static void main(String[] args) {
        System.out.println(getRandomMail());
    }*/
}