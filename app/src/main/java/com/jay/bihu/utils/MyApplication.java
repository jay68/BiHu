package com.jay.bihu.utils;

import android.app.Application;
import android.content.Context;

/**
 * Created by Jay on 2017/1/17.
 */

public class MyApplication extends Application {
    private static Context mContext;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }

    public static Context getContext() {
        return mContext;
    }
}
