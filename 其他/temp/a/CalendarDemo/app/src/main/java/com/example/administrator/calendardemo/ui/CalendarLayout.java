package com.example.administrator.calendardemo.ui;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by liaoruochen on 2017/4/14.
 * Description:
 */

public class CalendarLayout extends FrameLayout {

    private CalendarViewPager mCalendar;
    private int mCalendarHeight;
    private View mView;
    private float mDownX;
    private float mDownY;
    private boolean mIsClick;

    public CalendarLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        mCalendarHeight = mCalendar.getMeasuredHeight();

        Log.d("CalendarLayout", "mCalendar.getMeasuredHeight():" + mCalendarHeight);
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        mView.offsetTopAndBottom(mCalendarHeight);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        boolean isflag = false;

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = ev.getX();
                mDownY = ev.getY();
                mIsClick = isClickRecyclerView(mView, ev);
                break;

            case MotionEvent.ACTION_MOVE:
                float moveX = ev.getX();
                float moveY = ev.getY();

                float deltaX = moveX - mDownX;
                float deltaY = moveY - mDownY;

                if (Math.abs(deltaY) > 5 && Math.abs(deltaY) > Math.abs(deltaX)) {
                    isflag=true;
                    if (mIsClick){
                        isScroll(mView);
                    }
                }
                break;

            case MotionEvent.ACTION_UP:

                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    private boolean isScroll(View view) {
        if (view instanceof RecyclerView){
            RecyclerView recyclerView = (RecyclerView) view;
            View childView = recyclerView.getChildAt(0);
            if (childView.getTop()!=0){
                return true;
            }else{
                if (recyclerView.getChildLayoutPosition(childView)!=0){
                    return  true;
                }
            }
        }

        return false;
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mCalendar = (CalendarViewPager) getChildAt(0);
        mView = getChildAt(1);
    }


    private boolean isClickRecyclerView(View view, MotionEvent ev) {
        Rect rect = new Rect();
        view.getHitRect(rect);
        boolean isClick = rect.contains(((int) ev.getX()), ((int) ev.getY()));
        Log.d("CalendarLayout", "isClick:" + isClick);
        return isClick;


    }
}
