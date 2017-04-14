package com.example.administrator.myapplication.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.example.administrator.myapplication.App;
import com.example.administrator.myapplication.entity.LocationInfo;
import com.example.administrator.myapplication.utils.Utils;


/**
 * Created by liaoruochen on 2017/3/22.
 * Description: 定位服务
 */

public class LocationService extends Service implements AMapLocationListener {
    public static final int MilSECOND = 1000;
    public static final int SECOND = 1 * MilSECOND;
    public static final int MINUTES = 60 * SECOND;
    private static final long LOCATION_INTERVAL = 5* MINUTES;//分钟
    public static final String TAG = "liao";
    private AMapLocationClient mapLocationClient;
    private AMapLocationClientOption mapLocationClientOption;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startLocation();
        return START_STICKY;
    }

    private void startLocation() {
        stopLocation();
        if (mapLocationClient == null) {
            mapLocationClient = new AMapLocationClient(this.getApplicationContext());
            mapLocationClientOption = new AMapLocationClientOption();
        }
//       设置连续监听
        mapLocationClientOption.setOnceLocationLatest(false);
//        不实用缓存
        mapLocationClientOption.setLocationCacheEnable(false);
//        定位间隔
        mapLocationClientOption.setInterval(3*1000);
//          绑定客户端
        mapLocationClient.setLocationOption(mapLocationClientOption);
//        监听位置变化
        mapLocationClient.setLocationListener(this);
//        开始定位
        mapLocationClient.startLocation();

    }

    private void stopLocation() {
        if (mapLocationClient != null) {
            mapLocationClient.stopLocation();
            mapLocationClient.onDestroy();

            mapLocationClient = null;
            mapLocationClientOption = null;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null&&aMapLocation.getErrorCode()==0) {
            String str = Utils.getLocationStr(aMapLocation);
            Log.d(TAG, "onLocationChanged:  这里有很多位置信息");
            if (mLocationChangeListener != null) {
                //客户端绑定后，更新位置信息通知给客户端
                //如果是第一次通知，客户端通过数据库查询以前的数据
                //之后的更新，单独通知客户端，客户端不再去数据库里面查询
                mLocationChangeListener.onLocationChanged(aMapLocation);
                Log.d(TAG, "通知客户端更新");
            }
            saveLocationInfo(aMapLocation);
        }
    }

    /**
     * 保存数据到数据库
     * @param aMapLocation
     */
    private void saveLocationInfo(AMapLocation aMapLocation) {
        LocationInfo locationInfo=new LocationInfo(aMapLocation);
        Log.d(TAG, "保存到数据库");
        App.liteOrm.save(locationInfo);
    }

    public class MyBinder extends Binder {
        public void setIsFirstLocation(boolean isFirstLocation) {
        }

        public Service getLocationSerivces() {
            return LocationService.this;
        }
    }

    @Override
    public void onDestroy() {
        stopLocation();
        super.onDestroy();
    }



    private OnLocationChangeListener mLocationChangeListener;

    public void setLocationChangeListener(OnLocationChangeListener locationChangeListener) {
        mLocationChangeListener = locationChangeListener;
    }
    public interface OnLocationChangeListener{
        void onLocationChanged(AMapLocation location);
    }

}

