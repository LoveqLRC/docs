package com.example.administrator.calendardemo.ui;

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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by liaoruochen on 2017/4/14.
 * Description:
 */

public class MonthView extends RecyclerView {

    private List<CalendarBean> mList=new ArrayList<>();
    private int mLastSelectPosition = -1;

    public MonthView(Context context) {
        super(context);
        setLayoutManager(new GridLayoutManager(context,7));
        setAdapter(new MonthViewAdapter());
    }

    class MonthViewAdapter extends RecyclerView.Adapter<MonthViewAdapter.ViewHolder>{

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_calendar, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            CalendarBean calendarBean = mList.get(position);
            holder.tv.setText(String.valueOf(calendarBean.day));
            holder.tv.setSelected(mLastSelectPosition == position);
//            如果不是当月设置其他颜色
            if (calendarBean.mothFlag != 0) {
                holder.tv.setTextColor(0xff9299a1);
            } else {
                holder.tv.setTextColor(0xffffffff);
            }
        }

        @Override
        public int getItemCount() {
            return mList==null?0:mList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder{
            private final TextView tv;
            public ViewHolder(View itemView) {
                super(itemView);
                tv = ((TextView) itemView.findViewById(R.id.text));
                tv.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int mCurrentSelectPosition = getLayoutPosition();
                        if (mLastSelectPosition != -1 && mLastSelectPosition != mCurrentSelectPosition) {
//                            getChildAt(mLastSelectPosition).setSelected(false);
//                            getChildAt(mCurrentSelectPosition).setSelected(true);

                            getLayoutManager().findViewByPosition(mCurrentSelectPosition).setSelected(true);
                            getLayoutManager().findViewByPosition(mLastSelectPosition).setSelected(false);
                            notifyItemChanged(mLastSelectPosition);   //如果不调用的话，第一次刷新不了
                            notifyItemChanged(mCurrentSelectPosition);   //如果不调用的话，第一次刷新不了
//                            notifyDataSetChanged();
                            mLastSelectPosition = mCurrentSelectPosition;

//                            Log.d("ViewHolder", "getMarginTop():" + getMarginTop());
                        }
                    }
                });
            }
        }
    }

    public void updateData(List<CalendarBean> list,boolean isToday){
        mList.clear();
        mList.addAll(list);
        getAdapter().notifyDataSetChanged();
        resetSelectPosition(isToday);
    }

    private void resetSelectPosition(boolean isToday) {
        if (isToday) {
            int[] ymd = CalendarUtil.getYMD(new Date());
            mLastSelectPosition = ymd[2] + mList.get(0).first - 1;
        } else {
            mLastSelectPosition = mList.get(0).first;
        }
    }

}
