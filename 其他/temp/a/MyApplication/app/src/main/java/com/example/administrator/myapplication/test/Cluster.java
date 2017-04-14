package com.example.administrator.myapplication.test;

import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liaoruochen on 2017/3/24.
 * Description:
 */

public class Cluster {

    private LatLng mLatLng;
    private List<ClusterItem> mClusterItems;
    private Marker mMarker;

    public Cluster(LatLng mLatLng) {
        this.mLatLng = mLatLng;
        mClusterItems=new ArrayList<>();
    }

    public void addClusterItem(ClusterItem clusterItem){
        mClusterItems.add(clusterItem);
    }

    int getClusterCount(){return mClusterItems.size();}

    LatLng getCenterLatLng(){return mLatLng;}

    void setMarker(Marker marker){mMarker=marker;}

    Marker getMarker(){return  mMarker;}

    List<ClusterItem> getClusterItems(){return mClusterItems;}
}
