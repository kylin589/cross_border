package io.renren.modules.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @Auther: wdh
 * @Date: 2018/12/23 02:28
 * @Description:
 */
public class DateUtils {
    public static SimpleDateFormat startFormat = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
    public static SimpleDateFormat endFormat = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
    /**
     * 获取当天开始时间
     */
    public static String getDayFirst(){
        String first = startFormat.format(new Date());
        return first;
    }
    /**
     * 获取当天结束时间
     */
    public static String getDayEnd(){
        String end = endFormat.format(new Date());
        return end;
    }
    /**
     * 获取当前月第一天
     */
    public static String getMonthFirst(){
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, 0);
        //设置为1号,当前日期既为本月第一天
        c.set(Calendar.DAY_OF_MONTH,1);
        String first = startFormat.format(c.getTime());
        return first;
    }
    /**
     * 获取当前月最后一天
     */
    public static String getMonthEnd(){
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        String end = endFormat.format(c.getTime());
        return end;
    }
    /**
     * 获取当年的第一天
     * @return
     */
    public static String getYearFirst(){
        Calendar currCal=Calendar.getInstance();
        int currentYear = currCal.get(Calendar.YEAR);
        String start = startFormat.format(getYearFirst(currentYear));
        return start;
    }

    /**
     * 获取当年的最后一天
     * @return
     */
    public static String getYearEnd(){
        Calendar currCal=Calendar.getInstance();
        int currentYear = currCal.get(Calendar.YEAR);
        String end = endFormat.format(getYearLast(currentYear));
        return end;
    }
    /**
     * 获取某年第一天日期
     * @param year 年份
     * @return Date
     */
    public static Date getYearFirst(int year){
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        Date currYearFirst = calendar.getTime();
        return currYearFirst;
    }

    /**
     * 获取某年最后一天日期
     * @param year 年份
     * @return Date
     */
    public static Date getYearLast(int year){
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        calendar.roll(Calendar.DAY_OF_YEAR, -1);
        Date currYearLast = calendar.getTime();

        return currYearLast;
    }



    public static void main(String[] args) {
        System.out.println(getDayFirst());
        System.out.println(getDayEnd());
        System.out.println(getMonthFirst());
        System.out.println(getMonthEnd());
        System.out.println(getYearFirst());
        System.out.println(getYearEnd());
    }
}
