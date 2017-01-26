package com.jay.bihu.utils;

import android.os.Handler;
import android.widget.Toast;

import com.qiniu.android.common.Zone;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import cn.bmob.v3.AsyncCustomEndpoints;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.CloudCodeListener;

/**
 * Created by Jay on 2017/1/12.
 * 网络请求工具类
 */

public class HttpUtils {
    public interface Callback {
        void onResponse(Response response);

        void onFail(Exception e);
    }

    public static void loadImage(String address, Callback callback) {
        String name = address.substring(address.lastIndexOf('/') + 1);
        File file = new File(MyApplication.getContext().getExternalCacheDir(), name);
        if (file.exists()) {
            //文件存在则在文件中读取
            FileInputStream inputStream = null;
            try {
                inputStream = new FileInputStream(file);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                byte[] temp = new byte[1024];
                while (inputStream.read(temp) != -1)
                    outputStream.write(temp);
                callback.onResponse(new Response(outputStream.toByteArray()));
            } catch (IOException e) {
                e.printStackTrace();
                callback.onFail(e);
            } finally {
                if (inputStream != null)
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        callback.onFail(e);
                    }
            }
        } else {
            //文件不存在则在网络中读取
            sendHttpRequest(address, null, callback);
        }
    }

    public static void uploadImage(final byte[] data, final String name, final String param, final String address) {
        AsyncCustomEndpoints cloudCode = new AsyncCustomEndpoints();
        cloudCode.callEndpoint("getToken", new CloudCodeListener() {
            @Override
            public void done(Object o, BmobException e) {
                if (e == null) {
                    upload(data, name, param, address, o.toString());
                } else
                    Toast.makeText(MyApplication.getContext(), e.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private static void upload(byte[] data, String name, final String param, final String address, String token) {
        Configuration config = new Configuration.Builder().zone(Zone.zone2).build();
        UploadManager uploadManager = new UploadManager(config);
        uploadManager.put(data, name, token, new UpCompletionHandler() {
            @Override
            public void complete(String key, ResponseInfo info, JSONObject response) {
                if (info.isOK()) {
                    sendHttpRequest(address, param, new Callback() {
                        @Override
                        public void onResponse(Response response) {
                            if (response.isSuccess())
                                Toast.makeText(MyApplication.getContext(), "上传图片成功", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(MyApplication.getContext(), response.message(), Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onFail(Exception e) {
                            Toast.makeText(MyApplication.getContext(), e.toString(), Toast.LENGTH_LONG).show();
                        }
                    });
                } else
                    Toast.makeText(MyApplication.getContext(), info.error, Toast.LENGTH_LONG).show();
            }
        }, null);
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
                        final byte[] temp = read(connection.getInputStream());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                callback.onResponse(new Response(temp));
                            }
                        });

                        if (connection.getRequestMethod().equals("GET")) {
                            //缓存图片
                            String name = address.substring(address.lastIndexOf('/') + 1);
                            File file = new File(MyApplication.getContext().getExternalCacheDir(), name);
                            FileOutputStream os = new FileOutputStream(file);
                            os.write(temp);
                            os.close();
                        }
                    } else throw new Exception("无法连接服务器");
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

    private static byte[] read(InputStream is) throws IOException {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] temp = new byte[1024];
        int len;
        while ((len = is.read(temp)) != -1)
            outputStream.write(temp, 0, len);
        is.close();
        return outputStream.toByteArray();
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
                if (JsonParser.getElement(rawData, "data") != null)
                    mData = JsonParser.getElement(rawData, "data").getBytes();
                else mData = null;
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
