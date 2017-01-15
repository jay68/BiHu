package com.jay.bihu.utils;

import com.jay.bihu.data.Question;
import com.jay.bihu.data.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Jay on 2017/1/13.
 */

public class JsonParser {
    public static User getUser(String data) {
        User user = new User();
        try {
            JSONObject object = new JSONObject(data);
            user.setId(object.getInt("id"));
            user.setAvatar(object.getString("avatar"));
            user.setToken(object.getString("token"));
            user.setUsername(object.getString("username"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return user;
    }

    public static ArrayList<Question> getQuestionList(String data) {
        ArrayList<Question> questionList = new ArrayList<>();

        try {
            JSONObject object = new JSONObject(data);
            JSONArray array = object.getJSONArray("questions");
            for (int i = 0; i < array.length(); i++) {
                Question question = new Question();
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
