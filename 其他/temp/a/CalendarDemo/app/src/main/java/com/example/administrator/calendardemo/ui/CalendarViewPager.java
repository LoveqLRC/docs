package com.example.administrator.calendardemo.ui;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by liaoruochen on 2017/4/14.
 * Description:
 */

public class CalendarViewPager extends ViewPager {

    public CalendarViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = 0;
        View child = getChildAt(0);
        if (child != null) {
            child.measure(widthMeasureSpec, heightMeasureSpec);
            height = child.getMeasuredHeight();
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
