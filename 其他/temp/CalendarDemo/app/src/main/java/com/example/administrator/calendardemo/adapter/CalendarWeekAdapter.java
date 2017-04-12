package com.example.administrator.calendardemo.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by liaoruochen on 2017/4/12.
 * Description:
 */

public class CalendarWeekAdapter extends PagerAdapter {


    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        TextView textView=new TextView(container.getContext());
        textView.setGravity(Gravity.CENTER);
        textView.setText("这是第"+position+"个");
        container.addView(textView);
        return textView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
    }
}
