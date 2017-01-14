package com.jay.bihu.bean;

/**
 * Created by Jay on 2017/1/14.
 */

public class QuestionBean {
    /**
     * id : 1
     * title : 哦哈哟
     * content : 起床啦
     * date : 2016-12-15 12:16:09
     * recent : 2016-12-15 12:17:10
     * answerCount : 0
     * authorId : 1
     * exciting : 2
     * naive : 0
     * authorName : admin
     * authorAvatar : https://avatars1.githubusercontent.com/u/14852537?v=3&s=460
     */

    private int id;
    private String title;
    private String content;
    private String date;
    private String recent;
    private int answerCount;
    private int authorId;
    private int exciting;
    private int naive;
    private String authorName;
    private String authorAvatar;

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

    public int getExciting() {
        return exciting;
    }

    public void setExciting(int exciting) {
        this.exciting = exciting;
    }

    public int getNaive() {
        return naive;
    }

    public void setNaive(int naive) {
        this.naive = naive;
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
