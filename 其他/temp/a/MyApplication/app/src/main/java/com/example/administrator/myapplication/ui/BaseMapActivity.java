package com.example.administrator.myapplication.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.amap.api.maps.MapView;
import com.example.administrator.myapplication.R;

/**
 * Created by liaoruochen on 2017/3/23.
 * Description: MapView生命周期管理类，子类必须包含MapView
 */

public abstract class BaseMapActivity extends AppCompatActivity {

    protected MapView mMapView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        mMapView = ((MapView) findViewById(R.id.mapView));
        if (mMapView == null) {
            throw new IllegalStateException("子类必须包含MapView");
        }

        mMapView.onCreate(savedInstanceState);
    }

    /**
     * 返回布局Id
     * @return
     */
    protected abstract int getLayoutId();


    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }
}
