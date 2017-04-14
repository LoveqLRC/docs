package com.example.administrator.calendardemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.administrator.calendardemo.widget.CalendarViewPager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
//    @BindView(R.id.myvp)
//    ViewPager mVp;
//
//    @BindView(R.id.toggle)
//    Button mToggle;
//
    @BindView(R.id.vp)
    CalendarViewPager mVp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
//        int[] ymd = CalendarUtil.getYMD(new Date());
//        mVp.setAdapter(new CalendarWeekAdapter(ymd));
//        mVp.setCurrentItem(Integer.MAX_VALUE / 2, false);
    }

//    @OnClick(R.id.toggle)
//    public void onViewClicked() {
//        Toast.makeText(this, "onClick", Toast.LENGTH_SHORT).show();
//        mVp.setAdapter(new CalendarWeekAdapter());
//    }
}
