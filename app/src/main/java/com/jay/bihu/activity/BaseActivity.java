package com.jay.bihu.activity;

import android.app.Activity;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.jay.bihu.utils.ActivityCollector;

public class BaseActivity extends AppCompatActivity {
    protected Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this.getLocalClassName().replace("activity.", ""), this);
        mHandler = new Handler();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this.getLocalClassName().replace("activity.", ""));
    }

    protected void showMessage(String message, int length) {
        Toast.makeText(this, message, length).show();
    }

    protected void showMessage(String message) {
        showMessage(message, Toast.LENGTH_LONG);
    }
}
