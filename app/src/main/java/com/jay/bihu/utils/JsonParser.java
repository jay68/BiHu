package com.jay.bihu.utils;

import com.jay.bihu.bean.UserBean;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Jay on 2017/1/13.
 */

public class JsonParser {
    public static UserBean getUser(String data) {
        UserBean userBean = new UserBean();
        try {
            JSONObject object = new JSONObject(data);
            userBean.setId(object.getInt("id"));
            userBean.setAvatar(object.getString("avatar"));
            userBean.setToken(object.getString("token"));
            userBean.setUsername(object.getString("username"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return userBean;
    }
}
