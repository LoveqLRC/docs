package com.example.administrator.myapplication;

import android.app.Application;
import android.util.Log;

import com.amitshekhar.DebugDB;
import com.litesuits.orm.LiteOrm;

/**
 * Created by liaoruochen on 2017/3/22.
 * Description:
 */

public class App extends Application {
    public static final String DB_NAME = "location.db";
    public static LiteOrm liteOrm;

    @Override
    public void onCreate() {
        super.onCreate();
        liteOrm = LiteOrm.newSingleInstance(this, "location.db");
        liteOrm.setDebugged(true);
        Log.d("Appas", DebugDB.getAddressLog());
    }
}
