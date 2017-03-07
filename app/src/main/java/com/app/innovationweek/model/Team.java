package com.app.innovationweek.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by zeeshan on 3/7/2017.
 */
@Entity
public class Team {
    @Id
    private long id;
    private String name;
    @Generated(hash = 2059740694)
    public Team(long id, String name) {
        this.id = id;
        this.name = name;
    }
    @Generated(hash = 882286361)
    public Team() {
    }
    public long getId() {
        return this.id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
