package com.example.administrator.calendardemo.widget;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.calendardemo.R;
import com.example.administrator.calendardemo.entity.CalendarBean;

import java.util.List;

/**
 * Created by liaoruochen on 2017/4/12.
 * Description:
 */

public class CalendarWeekView extends RecyclerView {

    private final List<CalendarBean> calendarBeanList;
    private int selectPosition = -1;

    public CalendarWeekView(Context context,List<CalendarBean> calendarBeanList) {
        super(context);
        this.calendarBeanList = calendarBeanList;
        initSelectPosition();
        setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
    }

    private void initSelectPosition() {

    }

    public class CalendarWeekAdapter extends RecyclerView.Adapter<CalendarWeekAdapter.ViewHolder> {

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
            return 0;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private final TextView tv;
            public ViewHolder(View itemView) {
                super(itemView);
                tv = ((TextView) itemView.findViewById(R.id.text));
            }
        }
    }
}
