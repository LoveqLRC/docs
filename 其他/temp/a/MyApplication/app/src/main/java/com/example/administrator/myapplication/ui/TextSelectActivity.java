package com.example.administrator.myapplication.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.administrator.myapplication.App;
import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.adapter.TextSelectAdpater;
import com.example.administrator.myapplication.entity.LocationInfo;
import com.litesuits.orm.db.assit.QueryBuilder;

import java.util.ArrayList;

public class TextSelectActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    public  static String SELECT_KEY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_select);
        final String desc = getIntent().getStringExtra(SELECT_KEY);
        setTitle("最近轨迹("+desc+")");
        QueryBuilder query = new QueryBuilder(LocationInfo.class);
        query.distinct(true);
        query.columns(new String[]{"day"});
        query.appendOrderDescBy("day");
        final ArrayList<LocationInfo> mlist = App.liteOrm.query(query);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        TextSelectAdpater adapter = new TextSelectAdpater(mlist);
        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new TextSelectAdpater.OnItemClickListener() {
            @Override
            public void onClick(View v, int position) {
                Intent intent;
                if (desc.equals("文字")) {
                    intent=new Intent(TextSelectActivity.this,TextActivity.class);
                    intent.putExtra(TextActivity.DAY,mlist.get(position).getDay());
                }else{
                    intent=new Intent(TextSelectActivity.this,PicActivity.class);
                    intent.putExtra(PicActivity.DAY,mlist.get(position).getDay());
                }

                startActivity(intent);
            }
        });
    }
}
