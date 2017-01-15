package com.jay.bihu.utils;

import android.os.Handler;

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
 */

public class HttpUtils {
    public interface Callback {
        void onResponse(Response response);

        void onFail(IOException e);
    }

    public static void sendHttpRequest(final String address, final String content, final Callback callback) {
        final Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(address);
                    connection = (HttpURLConnection) url.openConnection();
                    if (content == null)
                        connection.setRequestMethod("GET");
                    else
                        connection.setRequestMethod("POST");
                    connection.setReadTimeout(5 * 1000);
                    connection.setConnectTimeout(10 * 1000);
                    connection.setDoOutput(true);

                    //post
                    if (content != null) {
                        OutputStream os = connection.getOutputStream();
                        os.write(content.getBytes());
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
                    }
                } catch (final IOException e) {
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

        public Response(String response) {
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
