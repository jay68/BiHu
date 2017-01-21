package com.jay.bihu.utils;

import android.os.Handler;

import com.qiniu.android.common.Zone;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
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

    public static void uploadFileToQiniu(byte[] data, String name, UpCompletionHandler upCompletionHandler) {
        Configuration config = new Configuration.Builder().zone(Zone.zone2).build();
        String token = "IyrjaIn4lQlsS2o4rYdZJNoMpbpPcx0AzBM_HdJK:ElHspM3v-N1FI1-4R1pj_-PPr1M=:eyJzY29wZSI6ImltYWdlcyIsImRlYWRsaW5lIjoxNDg1MDQ1ODg4fQ==";
        UploadManager uploadManager = new UploadManager(config);
        uploadManager.put(data, name, token, upCompletionHandler, null);
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
                    if (param == null)
                        connection.setRequestMethod("GET");
                    else {
                        connection.setRequestMethod("POST");
                        connection.setDoOutput(true);
                        OutputStream os = connection.getOutputStream();
                        os.write(param.getBytes());
                        os.flush();
                        os.close();
                    }
                    if (connection.getResponseCode() == 200) {
                        InputStream is = connection.getInputStream();
                        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                        byte[] temp = new byte[1024];
                        while (is.read(temp) != -1)
                            outputStream.write(temp);
                        is.close();
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                callback.onResponse(new Response(outputStream.toByteArray()));
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
        private String mInfo;
        private byte[] mData;

        Response(byte[] response) {
            String rawData = new String(response);
            mInfo = JsonParser.getElement(rawData, "info");
            if (mInfo == null) {
                mStatus = 200;
                mData = response;
            } else {
                mStatus = Integer.parseInt(JsonParser.getElement(rawData, "status"));
                mData = JsonParser.getElement(rawData, "data").getBytes();
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
            return new String(mData);
        }

        public byte[] bodyBytes() {
            return mData;
        }
    }
}
