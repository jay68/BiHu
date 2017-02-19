package com.jay.bihu.utils;

import android.widget.Toast;

/**
 * Created by Jay on 2017/2/19.
 */

public class ToastUtils {
    public static void showError(String error) {
        Toast.makeText(MyApplication.getContext(), error, Toast.LENGTH_LONG).show();
    }

    public static void showHint(String result) {
        Toast.makeText(MyApplication.getContext(), result, Toast.LENGTH_SHORT).show();
    }
}
