package com.example.administrator.calendardemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.administrator.calendardemo.adapter.WeekPagerAdapter;
import com.example.administrator.calendardemo.demo.MyAdapter;
import com.example.administrator.calendardemo.ui.CalendarViewPager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.myvp)
    CalendarViewPager mVp;

    @BindView(R.id.list)
    RecyclerView mList;

//    @BindView(R.id.myvp)
//    ViewPager mVp;
//
//    @BindView(R.id.toggle)
//    Button mToggle;
//
//    @BindView(R.id.vp)
//    CalendarViewPager mVp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
//        int[] ymd = CalendarUtil.getYMD(new Date());
//        mVp.setAdapter(new MonthPagerAdapter());
        mVp.setAdapter(new WeekPagerAdapter());
        mVp.setCurrentItem(Integer.MAX_VALUE / 2, false);

        mList.setLayoutManager(new LinearLayoutManager(this));
        mList.setAdapter(new MyAdapter());



//        int[] ymd = CalendarUtil.getYMD(new Date());
//        mVp.setAdapter(new WeekPagerAdapter(ymd));
//        mVp.setCurrentItem(Integer.MAX_VALUE / 2, false);
    }

//    @OnClick(R.id.toggle)
//    public void onViewClicked() {
//        Toast.makeText(this, "onClick", Toast.LENGTH_SHORT).show();
//        mVp.setAdapter(new CalendarWeekAdapter());
//    }
}
