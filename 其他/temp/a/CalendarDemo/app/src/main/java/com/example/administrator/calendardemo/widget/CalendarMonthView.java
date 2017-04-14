package com.example.administrator.calendardemo.widget;

import android.content.Context;
import android.graphics.Rect;
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
    private int mLastSelectPosition = -1;

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
            mLastSelectPosition = ymd[2] + calendarBeanList.get(0).first - 1;
        } else {
            mLastSelectPosition = calendarBeanList.get(0).first;
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

                        int mCurrentSelectPosition = getLayoutPosition();
//                        int mCurrentSelectPosition = getAdapterPosition();
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

    /**
     *
     * @return 第一个返回item距离父控件的高，第二繁返回item的高度
     */
    public int[] getMarginTop() {
        Rect rect = new Rect();
        View view = getLayoutManager().findViewByPosition(mLastSelectPosition);
        view.getHitRect(rect);
//        Log.d("CalendarMonthView", "rect.bottom-rect.top:" + (rect.bottom - rect.top));
        return new int[]{rect.top,rect.bottom-rect.top};
    }

}
