package com.app.innovationweek.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Madeyedexter on 08-03-2017.
 */

public class Question implements Parcelable{
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

    private String questionId;
    private String statement;
    private String fibAnswer;
    private String imgUri;
    private long maxTime;
    private Long startTime;
    private Long endTime;
    private Map<String, Option> options;
    private boolean expired;

    public Question(Parcel in){
        questionId=in.readString();
        statement=in.readString();
        fibAnswer=in.readString();
        imgUri=in.readString();
        maxTime=in.readLong();
        startTime=in.readLong();
        endTime=in.readLong();
        options=new HashMap<>();
        in.readMap(options,Option.class.getClassLoader());
        boolean[] tmp=new boolean[1];
        in.readBooleanArray(tmp);
        expired=tmp[0];
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public String getQuestionId() {
        return this.questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getStatement() {
        return this.statement;
    }

    public void setStatement(String statement) {
        this.statement = statement;
    }

    public String getFibAnswer() {
        return this.fibAnswer;
    }

    public void setFibAnswer(String fibAnswer) {
        this.fibAnswer = fibAnswer;
    }

    public String getImgUri() {
        return this.imgUri;
    }

    public void setImgUri(String imgUri) {
        this.imgUri = imgUri;
    }

    public long getMaxTime() {
        return this.maxTime;
    }

    public void setMaxTime(long maxTime) {
        this.maxTime = maxTime;
    }

    public Map<String, Option> getOptions() {
        return options;
    }

    public void setOptions(Map<String, Option> options) {
        this.options = options;
    }

    public boolean getExpired() {
        return this.expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(questionId);
        parcel.writeString(statement);
        parcel.writeString(fibAnswer);
        parcel.writeString(imgUri);
        parcel.writeLong(maxTime);
        parcel.writeLong(startTime);
        parcel.writeLong(endTime);
        parcel.writeMap(options);
        parcel.writeBooleanArray(new boolean[]{expired});
    }
}
