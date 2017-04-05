package com.example.calendarlibrary;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by liaoruochen on 2017/4/5.
 * Description:
 */

public class CalendarUtil {
    public static int[] getYMD(Date date){
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        return new int[]{calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH)+1,calendar.get(Calendar.DATE)};
    }
}
