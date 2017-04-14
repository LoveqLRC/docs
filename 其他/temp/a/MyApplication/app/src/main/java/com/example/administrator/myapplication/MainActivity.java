package com.example.administrator.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.myapplication.service.LocationService;
import com.example.administrator.myapplication.ui.DemoActivity;
import com.example.administrator.myapplication.ui.PicOnMapActivity;
import com.example.administrator.myapplication.ui.SearchActivity;
import com.example.administrator.myapplication.ui.SelectActivity;
import com.example.administrator.myapplication.ui.TextSelectActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mText;
    private TextView mPic;
    private TextView mPicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startService(new Intent(this, LocationService.class));
        mText = (TextView) findViewById(R.id.text);
        mPic = (TextView) findViewById(R.id.pic);
        mPicture = (TextView) findViewById(R.id.picture);
        mPicture.setOnClickListener(this);
        findViewById(R.id.search).setOnClickListener(this);
        findViewById(R.id.demo).setOnClickListener(this);
        findViewById(R.id.select).setOnClickListener(this);

        mText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TextSelectActivity.class);
                intent.putExtra(TextSelectActivity.SELECT_KEY, "文字");
                startActivity(intent);
            }
        });

        mPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TextSelectActivity.class);
                intent.putExtra(TextSelectActivity.SELECT_KEY, "地图");
                startActivity(intent);
            }
        });

    }

    @Override
    public void onClick(View v) {
        Intent intent=null;
        switch (v.getId()) {
            case R.id.picture:
                intent = new Intent(this, PicOnMapActivity.class);
                break;
            case R.id.demo:
                intent = new Intent(this, DemoActivity.class);
                break;
            case R.id.search:
                intent=new Intent(this, SearchActivity.class);
                break;
            case R.id.select:
                intent=new Intent(this,SelectActivity.class);
                break;
        }
        startActivity(intent);
    }
}
