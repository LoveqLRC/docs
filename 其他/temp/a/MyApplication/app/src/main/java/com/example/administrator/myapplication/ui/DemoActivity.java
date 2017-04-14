package com.example.administrator.myapplication.ui;

import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.administrator.myapplication.R;

public class DemoActivity extends AppCompatActivity {

    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI.buildUpon().
                appendPath("10703").build();
        mImageView = (ImageView) findViewById(R.id.imageView);
        Glide.with(this).load(uri).into(mImageView);
    }
}
