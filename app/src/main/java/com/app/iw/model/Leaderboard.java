package com.app.iw.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by 1036870 on 3/10/2017.
 */
@Entity
public class Leaderboard {
    @Id
    private String id;
    private String type;
    @Generated(hash = 137228492)
    public Leaderboard(String id, String type) {
        this.id = id;
        this.type = type;
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
    public String getType() {
        return this.type;
    }
    public void setType(String type) {
        this.type = type;
    }

}
