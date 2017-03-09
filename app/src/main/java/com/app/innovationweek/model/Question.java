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
    @Generated(hash = 1364812897)
    public Question(String questionId, String statement, String fibAnswer,
            String imgUri, long maxTime) {
        this.questionId = questionId;
        this.statement = statement;
        this.fibAnswer = fibAnswer;
        this.imgUri = imgUri;
        this.maxTime = maxTime;
    }
    @Generated(hash = 1868476517)
    public Question() {
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
}
