package com.jay.bihu.utils;

import com.jay.bihu.bean.QuestionBean;
import com.jay.bihu.bean.UserBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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

    public static ArrayList<QuestionBean> getQuestionList(String data) {
        ArrayList<QuestionBean> questionList = new ArrayList<>();

        try {
            JSONObject object = new JSONObject(data);
            JSONArray array = object.getJSONArray("questions");
            for (int i = 0; i < array.length(); i++) {
                QuestionBean question = new QuestionBean();
                JSONObject js = array.getJSONObject(i);
                question.setId(js.getInt("id"));
                question.setTitle(js.getString("title"));
                question.setContent(js.getString("content"));
                question.setDate(js.getString("date"));
                question.setRecent(js.getString("recent"));
                question.setAnswerCount(js.getInt("answerCount"));
                question.setAuthorId(js.getInt("authorId"));
                question.setExciting(js.getInt("exciting"));
                question.setNaive(js.getInt("naive"));
                question.setAuthorName(js.getString("authorName"));
                question.setAuthorAvatar(js.getString("authorAvatar"));
                questionList.add(question);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return questionList;
    }

    public static String getElement(String data, String name) {
        try {
            return new JSONObject(data).getString(name);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
