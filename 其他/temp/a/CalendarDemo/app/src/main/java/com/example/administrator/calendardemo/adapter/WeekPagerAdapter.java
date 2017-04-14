package com.example.administrator.calendardemo.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.calendardemo.entity.CalendarBean;
import com.example.administrator.calendardemo.ui.WeekView;
import com.example.administrator.calendardemo.utils.CalendarUtil;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by liaoruochen on 2017/4/12.
 * Description:
 */

public class WeekPagerAdapter extends PagerAdapter {
    int[] ymd;
    int mLastSelectPosition = 1;
    LinkedList<WeekView> cache = new LinkedList<>();

    public WeekPagerAdapter() {
        ymd = CalendarUtil.getYMD(new Date());
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
        WeekView view;
        if (!cache.isEmpty()) {
            view = cache.removeFirst();
        } else {
            view = new WeekView(container.getContext());
        }
        int[] sevenDay = CalendarUtil.getSevenDay(-position + Integer.MAX_VALUE / 2);
        List<CalendarBean> weekOfDayList = CalendarUtil.getWeekOfDayList(sevenDay[0], sevenDay[1], sevenDay[2]);
        view.updateData(weekOfDayList,mLastSelectPosition - 1);
        container.addView(view);
        return view;

//        int[] sevenDay = CalendarUtil.getSevenDay(-position + Integer.MAX_VALUE / 2);
//
//        List<CalendarBean> weekOfDayList = CalendarUtil.getWeekOfDayList(sevenDay[0], sevenDay[1], sevenDay[2]);
//
//        CalendarWeekView calendarWeekView = new CalendarWeekView(container.getContext(), weekOfDayList, mLastSelectPosition - 1);
//        container.addView(calendarWeekView);
//        return calendarWeekView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        cache.addLast((WeekView) object);
    }
}
