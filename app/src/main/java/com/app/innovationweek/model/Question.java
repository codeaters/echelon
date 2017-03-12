package com.app.innovationweek.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;

import java.util.Map;

/**
 * Created by Madeyedexter on 08-03-2017.
 */

@Entity
public class Question {
    @Id
    private String questionId;
    private String statement;
    @Transient
    private Map<String, Option> options;
    private String fibAnswer;
    private String imgUri;
    private long maxTime;
    private Long startTime;
    private Long endTime;
    private boolean expired;

    @Generated(hash = 1212470564)
    public Question(String questionId, String statement, String fibAnswer,
                    String imgUri, long maxTime, Long startTime, Long endTime,
                    boolean expired) {
        this.questionId = questionId;
        this.statement = statement;
        this.fibAnswer = fibAnswer;
        this.imgUri = imgUri;
        this.maxTime = maxTime;
        this.startTime = startTime;
        this.endTime = endTime;
        this.expired = expired;
    }

    @Generated(hash = 1868476517)
    public Question() {
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
}
