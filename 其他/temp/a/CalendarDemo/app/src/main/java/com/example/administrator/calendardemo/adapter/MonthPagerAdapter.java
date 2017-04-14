package com.example.administrator.calendardemo.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.calendardemo.entity.CalendarBean;
import com.example.administrator.calendardemo.ui.MonthView;
import com.example.administrator.calendardemo.utils.CalendarUtil;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by liaoruochen on 2017/4/14.
 * Description:
 */

public class MonthPagerAdapter extends PagerAdapter {
    LinkedList<MonthView> cache = new LinkedList<>();
    int[] ymd;

    public MonthPagerAdapter() {
        ymd=CalendarUtil.getYMD(new Date());
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
        MonthView view;
        if (!cache.isEmpty()) {
            view = cache.removeFirst();
        } else {
            view = new MonthView(container.getContext());
        }

        List<CalendarBean> dayList = CalendarUtil.getMonthOfDayList(ymd[0], ymd[1] + position - Integer.MAX_VALUE / 2);
        view.updateData(dayList,position == Integer.MAX_VALUE / 2);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        cache.addLast((MonthView) object);
    }
}
