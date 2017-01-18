package com.jay.bihu.data;

import java.util.ArrayList;

/**
 * Created by Jay on 2017/1/14.
 * 保存问题信息的类
 */

public class Question {
    /**
     * id : 1
     * title : 哦哈哟
     * content : 起床啦
     * date : 2016-12-15 12:16:09
     * recent : 2016-12-15 12:17:10
     * answerCount : 0
     * authorId : 1
     * excitingCount : 2
     * naiveCount : 0
     * authorName : admin
     * authorAvatar : https://avatars1.githubusercontent.com/u/14852537?v=3&s=460
     */

    //原键
    private int id;
    private String title;
    private String content;
    private String date;
    private String recent;
    private int answerCount;
    private int authorId;
    private int excitingCount;
    private int naiveCount;
    private String authorName;
    private String authorAvatar;
    private ArrayList<String> imageUrls = new ArrayList<>();

    //自定义键
    private boolean isNaive;
    private boolean isExciting;

    public boolean isFavorite() {
        return isFavorite;
    }

    public String getImageUrl(int index) {
        return imageUrls.get(index);
    }

    public void addImageUrl(String url) {
        imageUrls.add(url);
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public boolean isNaive() {
        return isNaive;
    }

    public void setNaive(boolean naive) {
        isNaive = naive;
    }

    public boolean isExciting() {
        return isExciting;
    }

    public void setExciting(boolean exciting) {
        isExciting = exciting;
    }

    private boolean isFavorite;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRecent() {
        return recent;
    }

    public void setRecent(String recent) {
        this.recent = recent;
    }

    public int getAnswerCount() {
        return answerCount;
    }

    public void setAnswerCount(int answerCount) {
        this.answerCount = answerCount;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public int getExcitingCount() {
        return excitingCount;
    }

    public void setExcitingCount(int excitingCount) {
        this.excitingCount = excitingCount;
    }

    public int getNaiveCount() {
        return naiveCount;
    }

    public void setNaiveCount(int naiveCount) {
        this.naiveCount = naiveCount;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorAvatar() {
        return authorAvatar;
    }

    public void setAuthorAvatar(String authorAvatar) {
        this.authorAvatar = authorAvatar;
    }
}
