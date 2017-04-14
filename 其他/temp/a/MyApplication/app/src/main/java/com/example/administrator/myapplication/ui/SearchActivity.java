package com.example.administrator.myapplication.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.example.administrator.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements TextWatcher, Inputtips.InputtipsListener, AMapLocationListener {
    private AutoCompleteTextView mKeywordText;
    private List<Tip> autoTip;//查询数据返回列表
    private String city;
    private AMapLocationClient aMapLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mKeywordText = (AutoCompleteTextView) findViewById(R.id.keyWord);
        mKeywordText.addTextChangedListener(this);
        mKeywordText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (autoTip != null && autoTip.size() > position) {
                    Tip tip=autoTip.get(position);
                    Toast.makeText(SearchActivity.this, "tip:" + tip, Toast.LENGTH_SHORT).show();
                }
            }
        });
        initLocation();
    }


    private void initLocation() {
//        初始化定位客户端
        aMapLocationClient = new AMapLocationClient(this);
//          设置定位参数
        aMapLocationClient.setLocationOption(getDefaultOption());
//        设置定位监听
        aMapLocationClient.setLocationListener(this);
//        开始定位
        aMapLocationClient.startLocation();
    }

    private AMapLocationClientOption getDefaultOption() {
        AMapLocationClientOption option = new AMapLocationClientOption();
//        高精度
        option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        option.setOnceLocation(true);

        return option;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        Log.d("SearchActivity", "beforeTextChanged");
        Log.d("SearchActivity", "s:" + s);

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        Log.d("SearchActivity", "onTextChanged");
        String result = s.toString().trim();
        if (result.length() > 0) {
            Log.d("SearchActivity", city);
            InputtipsQuery inputtipsQuery = new InputtipsQuery(result, city);
            inputtipsQuery.setCityLimit(true);
            Inputtips inputtips = new Inputtips(this, inputtipsQuery);
            inputtips.setInputtipsListener(this);
            inputtips.requestInputtipsAsyn();
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        Log.d("SearchActivity", "afterTextChanged");
    }

    @Override
    public void onGetInputtips(List<Tip> tipList, int code) {
        if (code == AMapException.CODE_AMAP_SUCCESS) {
            autoTip = tipList;
            List<String> listString = new ArrayList<String>();
            for (int i = 0; i < tipList.size(); i++) {
                listString.add(tipList.get(i).getName());
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    getApplicationContext(),
                    R.layout.route_inputs, listString);
            mKeywordText.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } else {
            Toast.makeText(SearchActivity.this, "erroCode " + code, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            city = aMapLocation.getCity();
            Log.d("SearchActivity ", "onLocationChanged" + city);
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
