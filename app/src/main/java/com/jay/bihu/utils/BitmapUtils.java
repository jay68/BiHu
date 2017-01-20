package com.jay.bihu.utils;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;

/**
 * Created by Jay on 2017/1/14.
 * 图片处理工具类
 */

public class BitmapUtils {
    public static Bitmap bytesToBitmap(byte[] src) {
        return BitmapFactory.decodeByteArray(src, 0, src.length);
    }

    public static String parseImageUri(Intent data) {
        if (Build.VERSION.SDK_INT >= 19)
            return handleImageOnKitKat(data);
        else return handleImageBeforeKitKat(data);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private static String handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(MyApplication.getContext(), uri)) {
            String docId = DocumentsContract.getDocumentId(uri);
            if (DocumentsContract.isDocumentUri(MyApplication.getContext(), uri)) {
                if ("com.android.provider.media.documents".equals(uri.getAuthority())) {
                    String id = docId.split(":")[1];
                    String selection = MediaStore.Images.Media._ID + "=" + id;
                    imagePath = getImagePath(uri, selection);
                }
            } else if ("com.android.provider.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            imagePath = getImagePath(uri, null);
        }
        return imagePath;
    }

    private static String handleImageBeforeKitKat(Intent data) {
        return getImagePath(data.getData(), null);
    }

    private static String getImagePath(Uri uri, String selection) {
        String path = null;
        Cursor cursor = MyApplication.getContext().getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst())
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            cursor.close();
        }
        return path;
    }
}
