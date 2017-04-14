package com.example.administrator.myapplication.test;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.LruCache;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.animation.AlphaAnimation;
import com.amap.api.maps.model.animation.Animation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liaoruochen on 2017/3/24.
 * Description:
 */

public class ClusterOverlay implements AMap.OnCameraChangeListener, AMap.OnMarkerClickListener {
    private final AMap aMap;
    private final int clusterSize;
    private final float scalePerPixel;
    private Context mContext;
    private List<ClusterItem> clusterItems;
    private final float distance;
    private HandlerThread markerHandlerThread;
    private HandlerThread signClusterThread;
    private List<Marker> mAddMarkers = new ArrayList<Marker>();
    private final LruCache<Integer, BitmapDescriptor> mLruCache;
    private ClusterRender mClusterRender;
    private AlphaAnimation mADDAnimation=new AlphaAnimation(0, 1);

    public ClusterOverlay(AMap aMap, List<ClusterItem> clusterItems, int clusterSize, Context context) {
        mLruCache = new LruCache<Integer, BitmapDescriptor>(80) {
            @Override
            protected void entryRemoved(boolean evicted, Integer key, BitmapDescriptor oldValue, BitmapDescriptor newValue) {
                oldValue.getBitmap().recycle();
            }
        };
        if (clusterItems != null) {
            this.clusterItems = clusterItems;
        } else {
            clusterItems = new ArrayList<>();
        }
        mContext = context;
        List<Cluster> clusters = new ArrayList<>();
        this.aMap = aMap;
        this.clusterSize = clusterSize;
        scalePerPixel = aMap.getScalePerPixel();
        distance = scalePerPixel * clusterSize;
        aMap.setOnCameraChangeListener(this);
        aMap.setOnMarkerClickListener(this);
        initThreadHandler();
    }

    private void initThreadHandler() {
        markerHandlerThread = new HandlerThread("addMarker");
        signClusterThread = new HandlerThread("calculateCluster");
        markerHandlerThread.start();
        signClusterThread.start();

    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        return false;
    }


    class MarkerHandler extends Handler {
        static final int ADD_CLUSTER_LIST = 0;

        static final int ADD_SINGLE_CLUSTER = 1;

        static final int UPDATE_SINGLE_CLUSTER = 2;

        public MarkerHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ADD_CLUSTER_LIST:
                    List<Cluster> clusters = (List<Cluster>) msg.obj;
                    addClusterToMap(clusters);
                    break;
                case ADD_SINGLE_CLUSTER:

                    break;
                case UPDATE_SINGLE_CLUSTER:

                    break;
            }
        }
    }

    /**
     * 将元素添加到地图上
     *
     * @param clusters
     */
    private void addClusterToMap(List<Cluster> clusters) {
        ArrayList<Marker> removeMarkers = new ArrayList<>();
        removeMarkers.addAll(mAddMarkers);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
        MyAnimationListener myAnimationListener = new MyAnimationListener(removeMarkers);
        for (Marker removeMarker : removeMarkers) {
            removeMarker.setAnimation(alphaAnimation);
            removeMarker.setAnimationListener(myAnimationListener);
            removeMarker.startAnimation();
        }

        for (Cluster cluster : clusters) {
            addSingleClusterToMap(cluster);
        }

    }

    /**
     * 将单个元素添加到地图
     *
     * @param cluster
     */
    private void addSingleClusterToMap(Cluster cluster) {
        LatLng centerLatLng = cluster.getCenterLatLng();
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.anchor(0.5f, 0.5f)
                .icon(getBitmapDes(cluster.getClusterCount()))
                .position(centerLatLng);
        Marker marker=aMap.addMarker(markerOptions);
        marker.setAnimation(mADDAnimation);
        marker.setObject(cluster);

        marker.startAnimation();
        cluster.setMarker(marker);
        mAddMarkers.add(marker);

    }

    /**
     * 获取每个点的绘制样式
     *
     * @param num
     * @return
     */
    private BitmapDescriptor getBitmapDes(int num) {
        BitmapDescriptor bitmapDescriptor = mLruCache.get(num);
        if (bitmapDescriptor == null) {
            TextView textView = new TextView(mContext);
            if (num > 1) {
                String tile = String.valueOf(num);
                textView.setText(tile);
            }
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(Color.BLACK);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            if (mClusterRender != null && mClusterRender.getDrawAble(num) != null) {
                textView.setBackgroundDrawable(mClusterRender.getDrawAble(num));
            } else {
//                textView.setBackgroundResource(R.drawable.defaultcluster);
            }
            bitmapDescriptor = BitmapDescriptorFactory.fromView(textView);
            mLruCache.put(num, bitmapDescriptor);
        }

        return bitmapDescriptor;
    }

    class MyAnimationListener implements Animation.AnimationListener {
        private List<Marker> mRemoveMarkers;

        public MyAnimationListener(List<Marker> mRemoveMarkers) {
            this.mRemoveMarkers = mRemoveMarkers;
        }

        @Override
        public void onAnimationStart() {

        }

        @Override
        public void onAnimationEnd() {
            for (Marker mRemoveMarker : mRemoveMarkers) {

                mRemoveMarker.remove();
            }
            mRemoveMarkers.clear();
        }
    }
}
