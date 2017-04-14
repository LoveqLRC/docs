package com.example.administrator.calendardemo.utils;


import com.example.administrator.calendardemo.entity.CalendarBean;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by liaoruochen on 2017/4/11.
 * Description:
 */

public class CalendarUtil {

    /**
     * 今天的日期
     *
     * @param date
     * @return //2017 4 11
     */
    public static int[] getYMD(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return new int[]{cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DATE)};
    }


    //查询周缓存
    private static HashMap<String, List<CalendarBean>> weekCache = new HashMap<>();


    public static List<CalendarBean> getWeekOfDayList(int y, int m, int d) {
        int weekOfMonth = getWeekOfMonth(y, m, d);
//        String key=y+m+d+"";
        String key = String.valueOf(y * 10000 + m * 100 + d);
        if (weekCache.containsKey(key)) {
            List<CalendarBean> calendarBeen = weekCache.get(key);
            if (calendarBeen == null || calendarBeen.isEmpty()) {
                weekCache.remove(key);
            } else {
                return calendarBeen;
            }
        }

        List<CalendarBean> monthOfDayList = getMonthOfDayList(y, m);
        List<CalendarBean> list = monthOfDayList.subList((weekOfMonth - 1) * 7, weekOfMonth * 7);
        weekCache.put(key, list);
        return list;
    }


    /**
     * @param y
     * @param m
     * @param day
     * @return 今天是第几周，例如2017413返回的是3(第三周),201741返回的是1(第一周)
     */
    public static int getWeekOfMonth(int y, int m, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(y, m - 1, day);
        return calendar.get(Calendar.WEEK_OF_MONTH);
    }

    private static HashMap<String, List<CalendarBean>> cache = new HashMap<>();


    /**
     * @param y
     * @param m
     * @return 返回一个月的集合
     */
    public static List<CalendarBean> getMonthOfDayList(int y, int m) {

        String key = y * 100 + "" + m;
        if (cache.containsKey(key)) {
            List<CalendarBean> list = cache.get(key);
            if (list == null) {
                cache.remove(key);
            } else {
                return list;
            }
        }

        List<CalendarBean> list = new ArrayList<CalendarBean>();
        cache.put(key, list);

        //计算出一月第一天是星期几
        int fweek = getDayOfWeek(y, m, 1);
        int total = getDayOfMonth(y, m);

        //根据星期推出前面还有几个显示
        for (int i = fweek - 1; i > 0; i--) {
            CalendarBean bean = getCalendarBean(y, m, 1 - i);
            bean.mothFlag = -1;
            bean.first = fweek - 1;
            list.add(bean);

        }

        //获取当月的天数
        for (int i = 0; i < total; i++) {
            CalendarBean bean = getCalendarBean(y, m, i + 1);
            bean.first = fweek - 1;
            list.add(bean);
        }

        //为了塞满42个格子，显示多出当月的天数
        for (int i = 0; i < 42 - (fweek - 1) - total; i++) {
            CalendarBean bean = getCalendarBean(y, m, total + i + 1);
            bean.mothFlag = 1;
            bean.first = fweek - 1;
            list.add(bean);
        }
        return list;
    }


    public static CalendarBean getCalendarBean(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day);
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DATE);

        CalendarBean bean = new CalendarBean(year, month, day);
        bean.week = getDayOfWeek(year, month, day);
//        String[] chinaDate = ChinaDate.getChinaDate(year, month, day);
//        bean.chinaMonth = chinaDate[0];
//        bean.chinaDay = chinaDate[1];

        return bean;
    }


    /**
     * @param y   年
     * @param m   月
     * @param day 日
     * @return 今天是星期几，例如2017411返回的是3(星期二),2017416返回的是1(星期日)
     */
    public static int getDayOfWeek(int y, int m, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(y, m - 1, day);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * @param y
     * @param m
     * @return 这个月总共有多少天，例如4月有30，5月有31
     */
    public static int getDayOfMonth(int y, int m) {
        Calendar cal = Calendar.getInstance();
        cal.set(y, m - 1, 1);
        int dateOfMonth = cal.getActualMaximum(Calendar.DATE);
        return dateOfMonth;
    }


    /**
     * 根据当天，获取前7天，或后7天
     *
     * @param position 正数获取前7天，负数获取后7天
     * @return
     */
    public static int[] getSevenDay(int position) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH, -7 * position);
        return new int[]{calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DATE)};
    }
}
