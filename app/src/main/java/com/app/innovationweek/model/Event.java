package com.app.innovationweek.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.app.innovationweek.model.dao.DaoSession;
import com.app.innovationweek.model.dao.EventDao;
import com.app.innovationweek.model.dao.PhaseDao;
import com.app.innovationweek.model.dao.RuleDao;
import com.google.firebase.database.IgnoreExtraProperties;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;


/**
 * Created by zeeshan on 3/8/2017.
 */
@Entity
@IgnoreExtraProperties
public class Event implements Parcelable {
    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };
    @Id
    private String id;
    private String imageUrl;
    private String name;
    private String description;
    private long startDate;
    private String quizId;
    @ToMany(referencedJoinProperty = "eventId")
    private List<Rule> rules;
    @ToMany(referencedJoinProperty = "eventId")
    private List<Phase> phases;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 1542254534)
    private transient EventDao myDao;


    protected Event(Parcel in) {
        id = in.readString();
        imageUrl = in.readString();
        name = in.readString();
        description = in.readString();
        startDate = in.readLong();
        quizId = in.readString();
        in.readTypedList(rules, Rule.CREATOR);
        in.readTypedList(phases, Phase.CREATOR);
    }

    @Generated(hash = 1633919326)
    public Event(String id, String imageUrl, String name, String description,
            long startDate, String quizId) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.quizId = quizId;
    }

    @Generated(hash = 344677835)
    public Event() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(imageUrl);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeLong(startDate);
        dest.writeString(quizId);
        dest.writeTypedList(rules);
        dest.writeTypedList(phases);
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getStartDate() {
        return this.startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public String getQuizId() {
        return this.quizId;
    }

    public void setQuizId(String quizId) {
        this.quizId = quizId;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1735897230)
    public List<Rule> getRules() {
        if (rules == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            RuleDao targetDao = daoSession.getRuleDao();
            List<Rule> rulesNew = targetDao._queryEvent_Rules(id);
            synchronized (this) {
                if (rules == null) {
                    rules = rulesNew;
                }
            }
        }
        return rules;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 53842526)
    public synchronized void resetRules() {
        rules = null;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 389475517)
    public List<Phase> getPhases() {
        if (phases == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            PhaseDao targetDao = daoSession.getPhaseDao();
            List<Phase> phasesNew = targetDao._queryEvent_Phases(id);
            synchronized (this) {
                if (phases == null) {
                    phases = phasesNew;
                }
            }
        }
        return phases;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 199272319)
    public synchronized void resetPhases() {
        phases = null;
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
    @Generated(hash = 1459865304)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getEventDao() : null;
    }


}
