package com.app.iw.model;

import com.app.iw.model.dao.DaoSession;
import com.app.iw.model.dao.TeamDao;
import com.app.iw.model.dao.UserDao;
import com.google.firebase.database.IgnoreExtraProperties;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToOne;

/**
 * Created by zeeshan on 3/7/2017.
 */
@Entity
@IgnoreExtraProperties
public class User {
    @Id
    private String id;
    private String username;
    private String name;
    private long teamId;
    private boolean canThinkQuick;

    @ToOne(joinProperty = "teamId")
    private Team team;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 1507654846)
    private transient UserDao myDao;
    @Generated(hash = 1834174654)
    private transient Long team__resolvedKey;


    @Generated(hash = 586692638)
    public User() {
    }

    @Generated(hash = 1513811421)
    public User(String id, String username, String name, long teamId,
                boolean canThinkQuick) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.teamId = teamId;
        this.canThinkQuick = canThinkQuick;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTeamId() {
        return this.teamId;
    }

    public void setTeamId(long teamId) {
        this.teamId = teamId;
    }

    public boolean getCanThinkQuick() {
        return this.canThinkQuick;
    }

    public void setCanThinkQuick(boolean canThinkQuick) {
        this.canThinkQuick = canThinkQuick;
    }

    /**
     * To-one relationship, resolved on first access.
     */
    @Generated(hash = 1193690988)
    public Team getTeam() {
        long __key = this.teamId;
        if (team__resolvedKey == null || !team__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            TeamDao targetDao = daoSession.getTeamDao();
            Team teamNew = targetDao.load(__key);
            synchronized (this) {
                team = teamNew;
                team__resolvedKey = __key;
            }
        }
        return team;
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 1078274893)
    public void setTeam(@NotNull Team team) {
        if (team == null) {
            throw new DaoException(
                    "To-one property 'teamId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.team = team;
            teamId = team.getId();
            team__resolvedKey = teamId;
        }
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

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 2059241980)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getUserDao() : null;
    }


}
