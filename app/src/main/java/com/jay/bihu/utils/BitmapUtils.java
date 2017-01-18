package com.jay.bihu.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by Jay on 2017/1/14.
 */

public class BitmapUtils {
    public static Bitmap getBitmap(byte[] src) {
        return BitmapFactory.decodeByteArray(src, 0, src.length);
    }

    public static Bitmap pikeBitmapFromStorage() {
        return null;
    }
}
