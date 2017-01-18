package com.jay.bihu.utils;

import android.os.Handler;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Jay on 2017/1/12.
 * 网络请求工具类
 */

public class HttpUtils {
    public interface Callback {
        void onResponse(Response response);

        void onFail(Exception e);
    }

    public static void sendHttpRequest(String address, String param) {
        sendHttpRequest(address, param, new Callback() {
            @Override
            public void onResponse(Response response) {

            }

            @Override
            public void onFail(Exception e) {

            }
        });
    }

    public static void sendHttpRequest(final String address, final String param, final Callback callback) {
        if (!NetWorkUtils.isNetworkConnected(MyApplication.getContext())) {
            callback.onFail(new Exception("无网络连接"));
            return;
        }
        final Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(address);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setReadTimeout(5 * 1000);
                    connection.setConnectTimeout(10 * 1000);
                    connection.setDoOutput(true);
                    if (param == null)
                        connection.setRequestMethod("GET");
                    else {
                        connection.setRequestMethod("POST");
                        OutputStream os = connection.getOutputStream();
                        os.write(param.getBytes());
                        os.flush();
                        os.close();
                    }
                    if (connection.getResponseCode() == 200) {
                        InputStream is = connection.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                        final StringBuilder response = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null)
                            response.append(line);
                        reader.close();
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                callback.onResponse(new Response(response.toString()));
                            }
                        });
                    } else throw new Exception("奇怪的错误");
                } catch (final Exception e) {
                    e.printStackTrace();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onFail(e);
                        }
                    });
                } finally {
                    if (connection != null)
                        connection.disconnect();
                }
            }
        }).start();
    }

    public static class Response {
        private int mStatus;
        private String mRawData;
        private String mInfo;
        private String mData;

        Response(String response) {
            mRawData = response;
            try {
                JSONObject object = new JSONObject(response);
                mInfo = object.getString("info");
                mStatus = object.getInt("status");
                mData = object.getString("data");
            } catch (JSONException e) {
                e.printStackTrace();
                mData = mRawData;
            }
        }

        public int getStatusCode() {
            return mStatus;
        }

        public boolean isSuccess() {
            return mStatus == 200;
        }

        public String getInfo() {
            return mInfo;
        }

        public String message() {
            return "status:" + mStatus + "\ninfo:" + mInfo;
        }

        public String bodyString() {
            return mData;
        }

        public byte[] bodyBytes() {
            return mData.getBytes();
        }
    }
}
