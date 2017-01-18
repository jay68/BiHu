package com.jay.bihu.data;

import android.graphics.Bitmap;

/**
 * Created by Jay on 2017/1/13.
 * 保存登录用户信息的类
 */

public class User {
    /**
     * id : 1
     * username : admin
     * avatarUrl : null
     * token : 6c5f989bdc56fe25f8a2b08443f354c910280c50
     */

    private int id;
    private String username;
    private String avatarUrl;
    private String token;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
