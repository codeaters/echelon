package com.app.innovationweek.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Madeyedexter on 08-03-2017.
 */

@Entity
public class Option {

    @Id
    private String optionId;

    private String questionId;

    public void setOptionValue(String optionValue) {
        this.optionValue = optionValue;
    }

    public void setCorrect(Boolean correct) {
        this.correct = correct;
    }

    public String getOptionId() {
        return optionId;
    }

    public String getOptionValue() {
        return optionValue;
    }

    public Boolean isCorrect() {
        return correct;
    }

    public void setOptionId(String optionId) {
        this.optionId = optionId;
    }

    public String getQuestionId() {
        return this.questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public Boolean getCorrect() {
        return this.correct;
    }

    private String optionValue;

    private Boolean correct;

    @Generated(hash = 2092050860)
    public Option(String optionId, String questionId, String optionValue,
            Boolean correct) {
        this.optionId = optionId;
        this.questionId = questionId;
        this.optionValue = optionValue;
        this.correct = correct;
    }

    @Generated(hash = 104107376)
    public Option() {
    }

}
