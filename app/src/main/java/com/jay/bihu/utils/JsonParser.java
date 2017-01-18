package com.jay.bihu.utils;

import android.util.Log;

import com.jay.bihu.data.Answer;
import com.jay.bihu.data.Question;
import com.jay.bihu.data.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Jay on 2017/1/13.
 * json解析类
 */

public class JsonParser {
    public static User getUser(String data) {
        User user = new User();
        try {
            JSONObject object = new JSONObject(data);
            user.setId(object.getInt("id"));
            user.setAvatarUrl(object.getString("avatar"));
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
                question.setAuthorName(js.getString("authorName"));
                question.setAuthorAvatar(js.getString("authorAvatar"));
                if (js.has("exciting"))
                    question.setExcitingCount(js.getInt("exciting"));
                if (js.has("naive"))
                    question.setNaiveCount(js.getInt("naive"));
                if (js.has("images")) {
                    JSONArray images = js.getJSONArray("images");
                    for (int j = 0; j < images.length(); j++)
                        question.addImageUrl(images.getString(i));
                }
                questionList.add(question);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.w("tag", e.toString());
        }
        return questionList;
    }

    public static ArrayList<Question> getFavoriteList(String data) {
        ArrayList<Question> questionList = getQuestionList(data.replace("\"answers\":", "\"questions\":"));
        for (Question question : questionList)
            question.setFavorite(true);
        return questionList;
    }

    public static ArrayList<Answer> getAnswer(String data) {
        ArrayList<Answer> answerList = new ArrayList<>();
        try {
            JSONObject object = new JSONObject(data);
            JSONArray array = object.getJSONArray("questions");
            for (int i = 0; i < array.length(); i++) {
                Answer answer = new Answer();
                JSONObject js = array.getJSONObject(i);
                answer.setId(js.getInt("id"));
                answer.setContent(js.getString("content"));
                answer.setDate(js.getString("date"));
                answer.setAuthorId(js.getInt("authorId"));
                answer.setAuthorName(js.getString("authorName"));
                answer.setAuthorAvatar(js.getString("authorAvatar"));
                answer.setExcitingCount(js.getInt("exciting"));
                answer.setNaiveCount(js.getInt("naive"));
                answer.setBest(js.getInt("best") == 1);
                if (js.has("images")) {
                    JSONArray images = js.getJSONArray("images");
                    for (int j = 0; j < images.length(); j++)
                        answer.addImageUrl(images.getString(i));
                }
                answerList.add(answer);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.w("tag", e.toString());
        }
        return answerList;
    }

    public static String getElement(String data, String name) {
        try {
            return new JSONObject(data).getString(name);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.w("tag", e.toString());
        }
        return null;
    }
}
