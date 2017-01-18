package com.jay.bihu.data;

import java.util.ArrayList;

/**
 * Created by Jay on 2017/1/18.
 * 保存回答信息的类
 */

public class Answer {
    /**
     * id : 1
     * content : 早啊，单身狗
     * date : 2016-12-15 12:17:10
     * excitingCount : 0
     * naiveCount : 0
     * best : 1
     * authorId : 2
     * authorName : test
     * authorAvatar : null
     * images : []
     */

    private int id;
    private String content;
    private String date;
    private int excitingCount;
    private int naiveCount;
    private boolean best;
    private int authorId;
    private String authorName;
    private String authorAvatar;
    private ArrayList<String> imageUrls;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public boolean isBest() {
        return best;
    }

    public void setBest(boolean best) {
        this.best = best;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
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

    public String getImageUrl(int index) {
        return imageUrls.get(index);
    }

    public void addImageUrl(String url) {
        imageUrls.add(url);
    }
}
