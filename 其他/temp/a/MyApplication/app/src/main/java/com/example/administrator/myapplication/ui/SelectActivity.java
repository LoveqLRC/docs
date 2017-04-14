package com.example.administrator.myapplication.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.example.administrator.myapplication.R;

import java.util.List;

public class SelectActivity extends AppCompatActivity implements PoiSearch.OnPoiSearchListener, AMapLocationListener {

    private String[] items = {"住宅区", "学校", "楼宇", "商场" };
    private String searchType = items[0];
    private LatLonPoint searchLatlonPoint;
    private PoiSearch.Query query;
    private AMapLocationClient aMapLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        initLocation();



    }

    private void poiSearch() {
        // 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        query = new PoiSearch.Query("",searchType,"");

        query.setCityLimit(true);
        query.setPageSize(20);
        query.setPageNum(0);


        PoiSearch poiSearch=new PoiSearch(this, query);
        poiSearch.setOnPoiSearchListener(this);
        poiSearch.setBound(new PoiSearch.SearchBound(searchLatlonPoint, 1000, true));//
        poiSearch.searchPOIAsyn();
    }

    private void initLocation() {
        aMapLocationClient = new AMapLocationClient(this);
        AMapLocationClientOption aMapLocationClientOption=new AMapLocationClientOption();
        aMapLocationClientOption.setOnceLocation(true);
        aMapLocationClientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        aMapLocationClient.setLocationOption(aMapLocationClientOption);

        aMapLocationClient.setLocationListener(this);
        aMapLocationClient.startLocation();
    }

    @Override
    public void onPoiSearched(PoiResult poiResult, int resultCode) {
        if (resultCode== AMapException.CODE_AMAP_SUCCESS) {
            Log.d("SelectActivity", "onPoiSearched");
            if (poiResult!=null&&poiResult.getQuery()!=null) {
                if (poiResult.getQuery().equals(query)) {
                    List<PoiItem> pois = poiResult.getPois();
                    if (pois!=null&&pois.size()>0) {
                        Log.d("SelectActivity", "pois:" + pois);
                    }
                }
            }
        }

    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {

        if (aMapLocation != null && aMapLocation.getErrorCode() == 0) {

            LatLng curLatLng=new LatLng(aMapLocation.getLatitude(),aMapLocation.getLongitude());

            searchLatlonPoint = new LatLonPoint(curLatLng.latitude, curLatLng.longitude);

            poiSearch();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (aMapLocationClient != null) {
            aMapLocationClient.stopLocation();
            aMapLocationClient.onDestroy();
        }
        aMapLocationClient=null;
    }
}
