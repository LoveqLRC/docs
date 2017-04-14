package com.example.administrator.myapplication.ui;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.amap.api.location.AMapLocation;
import com.example.administrator.myapplication.App;
import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.adapter.TextAdapter;
import com.example.administrator.myapplication.entity.LocationInfo;
import com.example.administrator.myapplication.service.LocationService;
import com.example.administrator.myapplication.utils.Utils;
import com.litesuits.orm.db.assit.QueryBuilder;

import java.util.List;

public class TextActivity extends AppCompatActivity {
    public static String DAY;
    private RecyclerView mRecyclerView;
    private ServiceConnection mConn;
    private LocationService.MyBinder myBinder;
    private boolean isBindService;
    private TextAdapter textAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        String day = getIntent().getStringExtra(TextActivity.DAY);
        setTitle(day);

        String today = Utils.formatUTC(System.currentTimeMillis(), "yyyy-MM-dd");

        //获取数据库信息
        getLocationInfo(day);

        if (today.equals(day)) {//如果查看的是当天的信息，那么我们需要绑定服务，获取实时更新的数据
            bindLocationService();
        }

    }

    private void bindLocationService() {
        Intent intent=new Intent(this,LocationService.class);
        mConn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                myBinder = (LocationService.MyBinder) service;

                LocationService locationService = (LocationService) myBinder.getLocationSerivces();
                locationService.setLocationChangeListener(new LocationService.OnLocationChangeListener() {
                    @Override
                    public void onLocationChanged(AMapLocation location) {
                        LocationInfo locationInfo=new LocationInfo(location);
                        textAdapter.addLocationInfo(locationInfo);
                        mRecyclerView.scrollToPosition(0);
                    }

                });
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        bindService(intent,mConn, Context.BIND_AUTO_CREATE);
        isBindService = true;
    }


    /**
     * 获取某一天的定位数据
     * @param day
     */
    private void getLocationInfo(String day) {
        QueryBuilder queryBuilder=new QueryBuilder(LocationInfo.class);
        queryBuilder.where("day"+"= ?",day);//SELECT time,address  FROM location ORDER BY day DESC
        queryBuilder.columns(new String[]{"time","address"});
        queryBuilder.appendOrderDescBy("time");
        List<LocationInfo> result = App.liteOrm.query(queryBuilder);
        textAdapter = new TextAdapter(result);
        mRecyclerView.setAdapter(textAdapter);
    }



    @Override
    protected void onDestroy() {
        if (mConn != null&&isBindService) {
                unbindService(mConn);
            isBindService=false;
        }
        super.onDestroy();
    }
}
