package com.example.administrator.calendardemo.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by liaoruochen on 2017/4/14.
 * Description:
 */

public class CalendarLayout extends FrameLayout {

    private CalendarViewPager mCalendar;
    private View mListView;
    private int mCalendarHeight;
    public static final int TYPE_CLOSE = 0;
    public static final int TYPE_OPEN = 1;
    public int type = TYPE_CLOSE;

    public CalendarLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
//        mCalendarHeight = mCalendar.getMeasuredHeight();
//        if (mCalendarHeight != 0) {
//            int distance = sizeHeight - mCalendarHeight;
//            int listViewHeightSpec = MeasureSpec.makeMeasureSpec(distance, MeasureSpec.EXACTLY);
//            mListView.measure(widthMeasureSpec, listViewHeightSpec);
//        }

        int[] rect = getMarginTop();
        int listViewHeightSpec = MeasureSpec.makeMeasureSpec(sizeHeight- rect[1], MeasureSpec.EXACTLY);
        mListView.measure(widthMeasureSpec, listViewHeightSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        
        if (type == TYPE_CLOSE) {
            int[] rect = getMarginTop();

            int offset = -rect[0];
            Log.d("CalendarLayout", "offset:" + offset);
            mCalendar.offsetTopAndBottom(offset);
            mListView.offsetTopAndBottom(rect[1]);
        } else {
            mListView.offsetTopAndBottom(mCalendarHeight);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mCalendar = (CalendarViewPager) getChildAt(0);
        mListView = getChildAt(1);
    }

    public int[] getMarginTop() {
        return mCalendar.getMarginTop();
    }
}
