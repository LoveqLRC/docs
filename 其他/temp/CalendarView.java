package com.example.calendarlibrary;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by liaoruochen on 2017/4/5.
 * Description:
 */

public class CalendarView extends ViewGroup {


    private int selectPostion=-1;

    private CaledarAdapter adapter;
    private List<CalendarBean> data;
    private OnItemClickListener onItemClickListener;

    private int row;
    private int column=7;
    private int itemWith;
    private int itemHeight;

    private boolean isToday;

    public interface OnItemClickListener{
        void onItemClick(View view, int position, CalendarBean bean);
    }

    public CalendarView(Context context, int row) {
        super(context);
        this.row=row;
    }

    public CalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public int getItemHeight() {
        return itemHeight;
    }

    public void setAdapter(CaledarAdapter adapter) {
        this.adapter = adapter;
    }

    public void setData(List<CalendarBean> data,boolean isToday){
        this.data=data;
        this.isToday=isToday;
        setItem();
    }

    private void setItem() {
        selectPostion=-1;
        if (adapter == null) {
            throw new RuntimeException("adapter is null");
        }
        for (int i = 0; i < data.size(); i++) {
            CalendarBean bean = data.get(i);
            View view = getChildAt(i);
            View childView = adapter.getView(view, this, bean);
            if (view ==null||view!=childView){
                addViewInLayout(childView,i,childView.getLayoutParams(),true);
            }

            if (isToday&&selectPostion==-1){

            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }
}
