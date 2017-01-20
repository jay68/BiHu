package com.jay.bihu.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Jay on 2017/1/14.
 * 保存问题信息的类
 */

public class Question implements Parcelable {
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
     * authorAvatarUrlString : https://avatars1.githubusercontent.com/u/14852537?v=3&s=460
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
    private String authorAvatarUrlString;
    private ArrayList<String> imageUrlStrings = new ArrayList<>();

    //自定义键
    private boolean isNaive;
    private boolean isExciting;

    public Question(){}

    private Question(Parcel in) {
        id = in.readInt();
        title = in.readString();
        content = in.readString();
        date = in.readString();
        recent = in.readString();
        answerCount = in.readInt();
        authorId = in.readInt();
        excitingCount = in.readInt();
        naiveCount = in.readInt();
        authorName = in.readString();
        authorAvatarUrlString = in.readString();
        imageUrlStrings = in.createStringArrayList();
        isNaive = in.readByte() != 0;
        isExciting = in.readByte() != 0;
        isFavorite = in.readByte() != 0;
    }

    public static final Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };

    public boolean isFavorite() {
        return isFavorite;
    }

    public String getImageUrl(int index) {
        return imageUrlStrings.get(index);
    }

    public void addImageUrl(String url) {
        imageUrlStrings.add(url);
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

    public String getAuthorAvatarUrlString() {
        return authorAvatarUrlString;
    }

    public void setAuthorAvatarUrlString(String authorAvatarUrlString) {
        this.authorAvatarUrlString = authorAvatarUrlString;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(content);
        dest.writeString(date);
        dest.writeString(recent);
        dest.writeInt(answerCount);
        dest.writeInt(authorId);
        dest.writeInt(excitingCount);
        dest.writeInt(naiveCount);
        dest.writeString(authorName);
        dest.writeString(authorAvatarUrlString);
        dest.writeStringList(imageUrlStrings);
        dest.writeByte((byte) (isNaive ? 1 : 0));
        dest.writeByte((byte) (isExciting ? 1 : 0));
        dest.writeByte((byte) (isFavorite ? 1 : 0));
    }
}
