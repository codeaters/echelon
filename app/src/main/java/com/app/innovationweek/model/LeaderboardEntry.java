package com.app.innovationweek.model;

import com.app.innovationweek.model.dao.DaoSession;
import com.app.innovationweek.model.dao.LeaderboardDao;
import com.app.innovationweek.model.dao.LeaderboardEntryDao;
import com.app.innovationweek.model.dao.UserDao;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;

import java.util.Comparator;

/**
 * Created by 1036870 on 3/10/2017.
 */
@Entity
public class LeaderboardEntry {
    public static Comparator<LeaderboardEntry> SCORE_DEC = new Comparator<LeaderboardEntry>() {
        @Override
        public int compare(LeaderboardEntry l1, LeaderboardEntry l2) {
            Float score = l1.getScore();
            return score.compareTo(l2.getScore());
        }
    };
    public static Comparator<LeaderboardEntry> RANK = new Comparator<LeaderboardEntry>() {
        @Override
        public int compare(LeaderboardEntry l1, LeaderboardEntry l2) {
            Integer rank = l1.getRank();
            return rank.compareTo(l2.getRank());
        }
    };
    public static Comparator<LeaderboardEntry> SCORE_DEC_TIME_ASC = new Comparator<LeaderboardEntry>
            () {
        @Override
        public int compare(LeaderboardEntry l1, LeaderboardEntry l2) {
            Float score = l1.getScore();
            int res = score.compareTo(l2.getScore());
            if (res == 0) {
                Long time = l1.getTotalTime();
                return time.compareTo(l2.getTotalTime());
            }
            return res;

        }
    };

    @Id(autoincrement = true)
    private Long id;
    private String leaderboardId;
    @ToOne(joinProperty = "leaderboardId")
    private Leaderboard leaderboard;
    private String userId;
    @ToOne(joinProperty = "userId")
    private User user;
    private float score;
    private long totalTime;
    private int correct;
    private int incorrect;
    private int rank;
    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /**
     * Used for active entity operations.
     */
    @Generated(hash = 683445952)
    private transient LeaderboardEntryDao myDao;
    @Generated(hash = 734411914)
    private transient String leaderboard__resolvedKey;
    @Generated(hash = 1867105156)
    private transient String user__resolvedKey;

    @Generated(hash = 947409762)
    public LeaderboardEntry(Long id, String leaderboardId, String userId, float score, long totalTime,
                            int correct, int incorrect, int rank) {
        this.id = id;
        this.leaderboardId = leaderboardId;
        this.userId = userId;
        this.score = score;
        this.totalTime = totalTime;
        this.correct = correct;
        this.incorrect = incorrect;
        this.rank = rank;
    }

    @Generated(hash = 1799948941)
    public LeaderboardEntry() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLeaderboardId() {
        return this.leaderboardId;
    }

    public void setLeaderboardId(String leaderboardId) {
        this.leaderboardId = leaderboardId;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public float getScore() {
        return this.score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public long getTotalTime() {
        return this.totalTime;
    }

    public void setTotalTime(long totalTime) {
        this.totalTime = totalTime;
    }

    public int getCorrect() {
        return this.correct;
    }

    public void setCorrect(int correct) {
        this.correct = correct;
    }

    public int getIncorrect() {
        return this.incorrect;
    }

    public void setIncorrect(int incorrect) {
        this.incorrect = incorrect;
    }

    public int getRank() {
        return this.rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    /**
     * To-one relationship, resolved on first access.
     */
    @Generated(hash = 1047939175)
    public Leaderboard getLeaderboard() {
        String __key = this.leaderboardId;
        if (leaderboard__resolvedKey == null || leaderboard__resolvedKey != __key) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            LeaderboardDao targetDao = daoSession.getLeaderboardDao();
            Leaderboard leaderboardNew = targetDao.load(__key);
            synchronized (this) {
                leaderboard = leaderboardNew;
                leaderboard__resolvedKey = __key;
            }
        }
        return leaderboard;
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 1670934944)
    public void setLeaderboard(Leaderboard leaderboard) {
        synchronized (this) {
            this.leaderboard = leaderboard;
            leaderboardId = leaderboard == null ? null : leaderboard.getId();
            leaderboard__resolvedKey = leaderboardId;
        }
    }

    /**
     * To-one relationship, resolved on first access.
     */
    @Generated(hash = 538271798)
    public User getUser() {
        String __key = this.userId;
        if (user__resolvedKey == null || user__resolvedKey != __key) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            UserDao targetDao = daoSession.getUserDao();
            User userNew = targetDao.load(__key);
            synchronized (this) {
                user = userNew;
                user__resolvedKey = __key;
            }
        }
        return user;
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 1065606912)
    public void setUser(User user) {
        synchronized (this) {
            this.user = user;
            userId = user == null ? null : user.getId();
            user__resolvedKey = userId;
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
    @Generated(hash = 1144773576)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getLeaderboardEntryDao() : null;
    }

}
