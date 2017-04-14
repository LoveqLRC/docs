package com.example.administrator.calendardemo.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.calendardemo.entity.CalendarBean;
import com.example.administrator.calendardemo.utils.CalendarUtil;
import com.example.administrator.calendardemo.widget.CalendarWeekView;

import java.util.List;

/**
 * Created by liaoruochen on 2017/4/12.
 * Description:
 */

public class CalendarWeekAdapter extends PagerAdapter {
    public int[] ymd;
    public int mLastSelectPosition=0;
    public CalendarWeekAdapter(int[] ymd) {
        this.ymd = ymd;
        int[] sevenDay = CalendarUtil.getSevenDay(0);
        mLastSelectPosition = CalendarUtil.getDayOfWeek(sevenDay[0], sevenDay[1], sevenDay[2]);

    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

//        int day = ymd[2] + (position - Integer.MAX_VALUE / 2) * 7;
//        List<CalendarBean> weekOfDayList = CalendarUtil.getWeekOfDayList(ymd[0], ymd[1], day);
        int[] sevenDay = CalendarUtil.getSevenDay(-position + Integer.MAX_VALUE / 2);

        List<CalendarBean> weekOfDayList = CalendarUtil.getWeekOfDayList(sevenDay[0], sevenDay[1], sevenDay[2]);

        CalendarWeekView calendarWeekView = new CalendarWeekView(container.getContext(), weekOfDayList, mLastSelectPosition-1);
        container.addView(calendarWeekView);
        return calendarWeekView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
