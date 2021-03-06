package com.example.administrator.calendardemo.widget;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.calendardemo.entity.CalendarBean;
import com.example.administrator.calendardemo.utils.CalendarUtil;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by liaoruochen on 2017/4/12.
 * Description:
 */

public class CalendarViewPager extends ViewPager {
    LinkedList<CalendarMonthView> cache = new LinkedList<>();

    SparseArray<View> views=new SparseArray<>();

    public CalendarViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        setCurrentItem(Integer.MAX_VALUE/2,false);
    }

    private void init() {
        final int[] ymd = CalendarUtil.getYMD(new Date());
        setAdapter(new PagerAdapter() {
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
                CalendarMonthView view;
//                if (!cache.isEmpty()) {
//                    view = cache.removeFirst();
//
//
//                } else {
                    List<CalendarBean> dayList = CalendarUtil.getMonthOfDayList(ymd[0], ymd[1] + position - Integer.MAX_VALUE / 2);
                    view = new CalendarMonthView(container.getContext(), dayList, position == Integer.MAX_VALUE / 2);
//                }

                container.addView(view);
                views.put(position, view);
                return view;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
//                cache.addLast((CalendarMonthView) object);
                views.remove(position);
            }
        });

        addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.d("CalendarViewPager", "position");
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = 0;
//        因为所有子view都是等高所以只能第一个
        View child = getChildAt(0);
        if (child != null) {
            child.measure(widthMeasureSpec, heightMeasureSpec);
            height = child.getMeasuredHeight();
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        View view = getChildAt(0);
//        Log.d("CalendarViewPager", "view.getMeasuredHeight():" + view.getMeasuredHeight());
    }

    /**
     *
     * @return 第一个返回item距离父控件的高，第二繁返回item的高度
     */
    public int[] getMarginTop(){
      View view= views.get(getCurrentItem());
        if (view instanceof CalendarMonthView){
            return ((CalendarMonthView) view).getMarginTop();
        }

        return new int[]{0,0};
    }
}
