package com.example.administrator.myapplication.ui;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Bundle;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.MarkerOptions;
import com.example.administrator.myapplication.App;
import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.entity.LocationInfo;
import com.example.administrator.myapplication.service.LocationService;
import com.example.administrator.myapplication.ui.BaseMapActivity;
import com.example.administrator.myapplication.utils.Utils;
import com.litesuits.orm.db.assit.QueryBuilder;

import java.util.List;

public class PicActivity extends BaseMapActivity implements AMap.OnMapLoadedListener {
    public static String DAY;
    private AMap aMap;
    private List<LocationInfo> infos;

    private boolean isBindService;
    private ServiceConnection mConn;
    private LocationService.MyBinder myBinder;
    private LatLngBounds.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String day = getIntent().getStringExtra(DAY);
        setTitle(day);
//        从数据库获取信息
        getLocationInfo(day);

//        根据数据库内容配置地图
        setupMap();

        String today = Utils.formatUTC(System.currentTimeMillis(), "yyyy-MM-dd");
        if (today.equals(day)) {//如果查看的是当天的信息，那么我们需要绑定服务，获取实时更新的数据
            bindLocationService();
        }


    }

    private void bindLocationService() {
        Intent intent = new Intent(this, LocationService.class);
        mConn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                myBinder = (LocationService.MyBinder) service;

                LocationService locationService = (LocationService) myBinder.getLocationSerivces();
                locationService.setLocationChangeListener(new LocationService.OnLocationChangeListener() {
                    @Override
                    public void onLocationChanged(AMapLocation location) {
                        if (builder != null) {
                            LocationInfo locationInfo = new LocationInfo(location);
                            aMap.addMarker(new MarkerOptions().position(new LatLng(locationInfo.getLatitude(), locationInfo.getLongitude()))
                                    .title(locationInfo.getPoiName())
                                    .snippet(locationInfo.getAddress()));
                        }
                    }

                });
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        bindService(intent, mConn, Context.BIND_AUTO_CREATE);
        isBindService = true;
    }

    private void setupMap() {
        if (infos == null || infos.isEmpty() || infos.size() == 0) {
            return;
        }
        if (aMap == null) {
            aMap = mMapView.getMap();
        }
        aMap.setOnMapLoadedListener(this);//图片加载完成回调，回调完成后添加标记物，及镜头转移
    }

    /**
     * 从数据库获取位置信息
     *
     * @param day
     */
    private void getLocationInfo(String day) {
        QueryBuilder queryBuilder = new QueryBuilder(LocationInfo.class);
        queryBuilder.where("day" + "= ?", day);
        queryBuilder.columns(new String[]{"longitude", "latitude", "poiName", "address"});
        queryBuilder.appendOrderDescBy("time");
        infos = App.liteOrm.query(queryBuilder);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_pic;
    }

    @Override
    public void onMapLoaded() {

        builder = new LatLngBounds.Builder();

        for (LocationInfo info : infos) {
            aMap.addMarker(new MarkerOptions().position(new LatLng(info.getLatitude(), info.getLongitude()))
                    .title(info.getPoiName())
                    .snippet(info.getAddress()));
            builder.include(new LatLng(info.getLatitude(), info.getLongitude()));
//            aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(info.getLatitude(),info.getLongitude()),16));
        }
        LatLngBounds build = builder.build();
        if (infos.size()==1){
            aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(infos.get(0).getLatitude(),infos.get(0).getLongitude()),16));

        }

        aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(build, 150));
    }

    @Override
    protected void onDestroy() {
        if (mConn != null && isBindService) {
            unbindService(mConn);
            isBindService = false;
        }
        super.onDestroy();
    }
}
