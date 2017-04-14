package com.example.administrator.myapplication.test;

import com.amap.api.maps.model.LatLng;

/**
 * Created by liaoruochen on 2017/3/24.
 * Description:
 */

public class RegionItem implements ClusterItem {
    private LatLng mlatLng;
    private String mTitle;

    public RegionItem(LatLng mlatLng, String mTitle) {
        this.mlatLng = mlatLng;
        this.mTitle = mTitle;
    }


    @Override
    public LatLng getPosition() {
        return mlatLng;
    }

    public String getTitle() {
        return mTitle;
    }
}
