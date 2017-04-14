package com.example.administrator.myapplication.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.ui.BaseMapActivity;

import java.util.ArrayList;
import java.util.List;

public class ClusterActivity extends BaseMapActivity implements AMap.OnMapLoadedListener, AMap.OnMarkerClickListener {

    private AMap amap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        if (amap == null) {
            amap = mMapView.getMap();
            amap.setOnMapLoadedListener(this);
//            amap.setOnMarkerClickListener(this);
        }

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_cluster;
    }

    @Override
    public void onMapLoaded() {
        new Thread(){
            @Override
            public void run() {
                List<ClusterItem> items=new ArrayList<ClusterItem>();

    //          随机点
                for (int i = 0; i < 10000; i++) {
                    double lat = Math.random() + 39.474923;
                    double lon = Math.random() + 116.027116;
//                不检查坐标点的合法性
                    LatLng latLng = new LatLng(lat, lon, false);

                    RegionItem regionItem =new RegionItem(latLng,"test"+i);

                    items.add(regionItem);
                }

            }
        }.run();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }
}
