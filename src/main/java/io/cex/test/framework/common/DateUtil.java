package io.cex.test.framework.common;

import lombok.extern.slf4j.Slf4j;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author shenqingyan
 * @create 2019/1/10 15:27
 * @desc 时间工具类
 **/
@Slf4j
public class DateUtil {
    /**
     * @desc 将Date类型的时间转换为制定格式的String类型
     * @param date Date对象
     * @param pattern 指定格式
     **/
    public static String dateToStr(Date date, String pattern) {
        if(pattern == null){
            pattern = "yyyy-MM-dd HH:mm:ss";
        }
        DateFormat ymdhmsFormat = new SimpleDateFormat(pattern);
        return ymdhmsFormat.format(date);
    }

    /**
     * @desc 将指定格式的字符串转换为Date类型的时间
     * @param str 时间字符串
     * @param pattern 指定格式
     **/
    public static Date strToDate(String str,String pattern) throws ParseException {
        if(pattern == null){
            pattern = "yyyy-MM-dd HH:mm:ss";
        }
        DateFormat ymdhmsFormat = new SimpleDateFormat(pattern);
        return ymdhmsFormat.parse(str);
    }

    /**
     * @desc 获得当天日期
     * @param
     **/
    public static Date getToday(){
        Calendar ca = Calendar.getInstance();
        return ca.getTime();
    }
    /**
     * @desc 获得格式化的当天日期
     * @param
     **/
    public static String getFomatToday(){
        Calendar ca = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = format.format(ca.getTime());
        return date;
    }

    /**
     * @desc local时间转UTC时间
     * @param
     **/
    public static Date localToUTC(String localTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date localDate= null;
        try {
            localDate = sdf.parse(localTime);
        } catch (ParseException e) {
            e.printStackTrace();
            log.error("time format fail");
        }
        long localTimeInMillis=localDate.getTime();
        Calendar calendar= Calendar.getInstance();
        calendar.setTimeInMillis(localTimeInMillis);
        int zoneOffset = calendar.get(java.util.Calendar.ZONE_OFFSET);
        int dstOffset = calendar.get(java.util.Calendar.DST_OFFSET);
        calendar.add(java.util.Calendar.MILLISECOND, -(zoneOffset + dstOffset));
        Date utcDate=new Date(calendar.getTimeInMillis());
        return utcDate;
    }

    /**
     * @desc 获得指定日期
     * @param year 年度
     * @param month 月份
     * @param date 日期
     **/
    public static Date mkDate(int year, int month, int date){
        Calendar ca = Calendar.getInstance();
        ca.set(year, month-1, date);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        sdf.format(ca.getTime());
        return ca.getTime();
    }

    /**
     * @desc 比较两个日期比较是否相等，只比较到分
     * @param date1 第一个日期
     * @param date2 第一个日期
     **/
    public static  boolean compareTwoDate(Date date1,Date date2) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd HH:mm");
        return simpleDateFormat.format(date1).equals(
                simpleDateFormat.format(date2));
    }

    /**
     * @desc 在当前日期上加几天
     * @param days 增加的天数
     **/
    public static String fewdaysPlus(int days){
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currdate = format.format(date);
        Calendar ca = Calendar.getInstance();
        ca.add(Calendar.DATE, days);
        date = ca.getTime();
        String enddate = format.format(date);
        return enddate;
    }
    /**
     * @desc 在指定日期上加几天
     * @param days 增加的天数
     * @param newDate 指定日期
     **/
    public static String fewdaysPlus(int days, String newDate){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date currdate = format.parse(newDate);
            Calendar ca = Calendar.getInstance();
            ca.add(Calendar.DATE, days);
            currdate= ca.getTime();
            String enddate = format.format(currdate);
            return enddate;
        }catch (ParseException e){
            e.getErrorOffset();
            log.error("time format fail");
        }
        return null;
    }
    /**
     * @desc 时间string转long
     * @param
     **/
    public static Long string2Long(String time){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        Long rsl = null;
        try {
            date = formatter.parse(time);
            rsl = date.getTime();
            return rsl;
        }catch (ParseException e){
            e.printStackTrace();
            log.error("time format fail");
        }
        return rsl;
    }


}
