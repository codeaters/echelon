package com.app.innovationweek.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by 1036870 on 3/10/2017.
 */
@Entity
public class Leaderboard {
    @Id
    private String id;

    @Generated(hash = 1527334501)
    public Leaderboard(String id) {
        this.id = id;
    }

    @Generated(hash = 1566480275)
    public Leaderboard() {
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
