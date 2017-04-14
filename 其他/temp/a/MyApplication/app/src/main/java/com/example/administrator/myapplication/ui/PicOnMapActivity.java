package com.example.administrator.myapplication.ui;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.CoordinateConverter;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.administrator.myapplication.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PicOnMapActivity extends BaseMapActivity implements LoaderManager.LoaderCallbacks<Cursor>, GeocodeSearch.OnGeocodeSearchListener, AMap.OnMapLoadedListener, AMap.OnMarkerClickListener {
    private GeocodeSearch geocoderSearch;
    private List<View> mImageOverlays = new ArrayList<>();
    private static final String[] STORE_IMAGES = {
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.LATITUDE,
            MediaStore.Images.Media.LONGITUDE,

            MediaStore.Images.Media._ID
    };
    private AMap aMap;
    private LatLngBounds.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupMap();


        geocoderSearch = new GeocodeSearch(this);
        geocoderSearch.setOnGeocodeSearchListener(this);//OnGeocodeSearchListener

    }

    private void setupMap() {
        if (aMap == null) {
            aMap = mMapView.getMap();
        }
        aMap.setOnMapLoadedListener(this);
        aMap.setOnMarkerClickListener(this);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_pic_on_map;
    }

    /**
     * 初始化CursorLoader
     */
    private void initCursorLoader() {
        Log.d("PicOnMapActivity", "initCursorLoader");
        LoaderManager loaderManager = getLoaderManager();
        if (loaderManager != null) {
            loaderManager.initLoader(0, null, this);//LoaderCallbacks接口
        }
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d("PicOnMapActivity", "onCreateLoader");
        CursorLoader cursorLoader = new CursorLoader(this,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                STORE_IMAGES,
                null,
                null,
                null
        );
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d("PicOnMapActivity", "onLoadFinished");
        Log.d("PicOnMapActivity", "data.getCount():" + data.getCount());
        handleDataFromCursor(data);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        mMapView.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                for (View mImageOverlay : mImageOverlays) {
//                    ImageView imageView = (ImageView) mImageOverlay.findViewById(R.id.imageView);
//                    Uri uri = (Uri) imageView.getTag(R.id.imageView);
//                    Glide.with(PicOnMapActivity.this).load(uri).placeholder(R.mipmap.ic_launcher).centerCrop().override(100,100)
//                            .crossFade().into(imageView);
//                }
//            }
//        },5000);
    }

    private void handleDataFromCursor(Cursor data) {
        Map<String, List<Integer>> latLngMap = new HashMap<>();
        List<Integer> picIdList = new ArrayList<>();
        if (data != null) {
            while (data.moveToNext()) {
                String name = data.getString(0);
                String latitude = data.getString(1);
                String longitude = data.getString(2);
                String id = data.getString(3);
//                如果照片没有经纬度信息，那么跳过
                if (TextUtils.isEmpty(latitude)) {
                    continue;
                }
//                把经纬度合在一起去重，concat不会修改原来的内容，返回值为新拼接的字符串
                String concat = latitude.concat("-").concat(longitude);

                List<Integer> integers = latLngMap.get(concat);
                if (integers == null) {
                    picIdList = new ArrayList<>();
                }

                picIdList.add(Integer.valueOf(id));
                latLngMap.put(concat, picIdList);


                Log.d("PicOnMapActivity", latitude + "       " + longitude + "        " + id);

            }

            Set<String> latlngSet = latLngMap.keySet();
            for (String latlngStr : latlngSet) {
                String[] split = latlngStr.split("-");
                String latitude = split[0];
                String longitude = split[1];
                Log.d("PicOnMapActivity", "latitude     " + latitude + "         longitude" + longitude);
                List<Integer> integers = latLngMap.get(latlngStr);
                Log.d("PicOnMapActivity", "integers:" + integers);
                //                将Gps经纬度转换为高德地图经纬度
                CoordinateConverter converter = new CoordinateConverter(this);
                converter.from(CoordinateConverter.CoordType.GPS);
                converter.coord(new LatLng(Double.valueOf(latitude), Double.valueOf(longitude)));
                final LatLng latLng = converter.convert();


                final MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                final View view = View.inflate(getApplicationContext(), R.layout.item_marker, null);
                final ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI.buildUpon().
                        appendPath(String.valueOf(integers.get(0))).build();

                imageView.setTag(R.id.imageView, uri);
//                imageView.setImageResource(R.mipmap.ic_launcher_round);
//                imageView.setImageResource(R.mipmap.ic_launcher_round);
//                imageView.setImageURI(uri);

                Glide.with(PicOnMapActivity.this).load(uri).centerCrop().override(100, 100)
                        .crossFade().into(new SimpleTarget<GlideDrawable>() {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        imageView.setImageDrawable(resource);
                        markerOptions.icon(BitmapDescriptorFactory.fromView(view));
                        aMap.addMarker(markerOptions);
                        //                限定地图的显示范围
                        builder.include(latLng);
                    }
                });

//            Glide.with(PicOnMapActivity.this).load(uri).into(im)

                //               向地图添加标记物


            }


        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.d("PicOnMapActivity", "onLoaderReset");
    }


    /**
     * 响应逆地理编码
     */
    public void getAddress(final LatLonPoint latLonPoint) {
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200,
                GeocodeSearch.GPS);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        geocoderSearch.getFromLocationAsyn(query);// 设置异步逆地理编码请求
    }

    /**
     * 逆地理编码回调
     */
    @Override
    public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getRegeocodeAddress() != null &&
                    result.getRegeocodeAddress().getFormatAddress() != null) {

            }
        }
    }

    /**
     * 地理编码查询回调
     */
    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }

    @Override
    public void onMapLoaded() {
        builder = new LatLngBounds.Builder();

        initCursorLoader();

        LatLngBounds build = builder.build();
        aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(build, 150));
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Toast.makeText(this, "marker", Toast.LENGTH_SHORT).show();
        return true;
    }
}
