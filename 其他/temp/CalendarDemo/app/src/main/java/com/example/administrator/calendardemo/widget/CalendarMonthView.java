package com.example.administrator.calendardemo.widget;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.calendardemo.R;
import com.example.administrator.calendardemo.entity.CalendarBean;
import com.example.administrator.calendardemo.utils.CalendarUtil;

import java.util.Date;
import java.util.List;

/**
 * Created by liaoruochen on 2017/4/12.
 * Description:
 */

public class CalendarMonthView extends RecyclerView {

    private final List<CalendarBean> calendarBeanList;
    private final boolean isToday;
    private int selectPosition = -1;

    public CalendarMonthView(Context context, List<CalendarBean> calendarBeanList, boolean isToday) {
        super(context);
        setLayoutManager(new GridLayoutManager(context, 7));
        this.calendarBeanList = calendarBeanList;
        this.isToday = isToday;
        initSelectPosition();
        setAdapter(new CalendarMonthAdapter());
    }

    private void initSelectPosition() {
        if (isToday) {
            int[] ymd = CalendarUtil.getYMD(new Date());
            selectPosition=ymd[2]+calendarBeanList.get(0).first-1;
        }else{
            selectPosition=calendarBeanList.get(0).first;
        }
    }

    class CalendarMonthAdapter extends RecyclerView.Adapter<CalendarMonthAdapter.ViewHolder> {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_calendar, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            CalendarBean calendarBean = calendarBeanList.get(position);
            holder.tv.setText(String.valueOf(calendarBean.day));
            holder.tv.setSelected(selectPosition==position);
//            如果不是当月设置其他颜色
            if (calendarBean.mothFlag != 0) {
                holder.tv.setTextColor(0xff9299a1);
            } else {
                holder.tv.setTextColor(0xffffffff);
            }
        }

        @Override
        public int getItemCount() {
            return calendarBeanList == null ? 0 : calendarBeanList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private final TextView tv;

            public ViewHolder(View itemView) {
                super(itemView);
                tv = ((TextView) itemView.findViewById(R.id.text));
                tv.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int layoutPosition = getLayoutPosition();
                        if (selectPosition != -1 && selectPosition != layoutPosition) {
                            getChildAt(selectPosition).setSelected(false);
                            getChildAt(layoutPosition).setSelected(true);
                            notifyItemChanged(selectPosition);   //如果不掉用的话，第一次刷新不了
                            selectPosition = layoutPosition;
                        }
                    }
                });
            }
        }
    }

}
