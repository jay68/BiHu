package com.jay.bihu.data;

import android.graphics.Bitmap;

/**
 * Created by Jay on 2017/1/13.
 */

public class User {
    /**
     * id : 1
     * username : admin
     * avatar : null
     * token : 6c5f989bdc56fe25f8a2b08443f354c910280c50
     */

    private int id;
    private String username;
    private String avatar;
    private String token;
    private Bitmap avatarBitmap;

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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Bitmap getAvatarBitmap() {
        return avatarBitmap;
    }

    public void setAvatarBitmap(Bitmap avatarBitmap) {
        this.avatarBitmap = avatarBitmap;
    }
}
