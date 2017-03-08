package com.app.innovationweek.model;

import android.graphics.Bitmap;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;
import org.greenrobot.greendao.DaoException;
import com.app.innovationweek.model.dao.DaoSession;
import com.app.innovationweek.model.dao.OptionDao;
import com.app.innovationweek.model.dao.QuestionDao;

/**
 * Created by Madeyedexter on 08-03-2017.
 */

@Entity
public class Question {

    public String getQuestionId() {
        return questionId;
    }


    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1617387998)
    public List<Option> getOptions() {
        if (options == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            OptionDao targetDao = daoSession.getOptionDao();
            List<Option> optionsNew = targetDao._queryQuestion_Options(questionId);
            synchronized (this) {
                if (options == null) {
                    options = optionsNew;
                }
            }
        }
        return options;
    }


    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }


    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 37457025)
    public synchronized void resetOptions() {
        options = null;
    }


    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }


    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }


    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }


    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 754833738)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getQuestionDao() : null;
    }


    public String getQuestionStatement() {
        return this.questionStatement;
    }


    public void setQuestionStatement(String questionStatement) {
        this.questionStatement = questionStatement;
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

    @Id
    private String questionId;

    private String questionStatement;

    @ToMany(referencedJoinProperty = "questionId")
    private List<Option> options;

    private String fibAnswer;
    private String imgUri;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 891254763)
    private transient QuestionDao myDao;
    @Generated(hash = 1492703747)
    public Question(String questionId, String questionStatement, String fibAnswer,
            String imgUri) {
        this.questionId = questionId;
        this.questionStatement = questionStatement;
        this.fibAnswer = fibAnswer;
        this.imgUri = imgUri;
    }


    @Generated(hash = 1868476517)
    public Question() {
    }

}
