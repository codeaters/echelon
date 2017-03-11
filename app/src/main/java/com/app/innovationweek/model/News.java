package com.app.innovationweek.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by n188851 on 10-03-2017.
 */

@Entity
@IgnoreExtraProperties
public class News implements Parcelable{

    public static final Creator<News> CREATOR = new Creator<News>() {
        @Override
        public News createFromParcel(Parcel in) {
            return new News(in);
        }

        @Override
        public News[] newArray(int size) {
            return new News[size];
        }
    };
    @Id
    private String newsId;
    private String content;
    private String color;
    private Long timestamp;
    private String imgUrl;

    protected News(Parcel in) {
    }

    @Generated(hash = 66375118)
    public News(String newsId, String content, String color, Long timestamp,
                String imgUrl) {
        this.newsId = newsId;
        this.content = content;
        this.color = color;
        this.timestamp = timestamp;
        this.imgUrl = imgUrl;
    }

    @Generated(hash = 1579685679)
    public News() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return "News{" +
                "newsId='" + newsId + '\'' +
                ", content='" + content + '\'' +
                ", color='" + color + '\'' +
                ", timestamp=" + timestamp +
                ", imgUrl='" + imgUrl + '\'' +
                '}';
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    public String getNewsId() {
        return this.newsId;
    }

    public void setNewsId(String newsId) {
        this.newsId = newsId;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getColor() {
        return this.color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Long getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getImgUrl() {
        return this.imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }


}
