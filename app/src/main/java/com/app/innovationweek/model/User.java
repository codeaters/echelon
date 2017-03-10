package com.app.innovationweek.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by zeeshan on 3/7/2017.
 */
@Entity
public class User {
    @Id
    private String id;
    private String name;
    private String team;
    @Generated(hash = 297613350)
    public User(String id, String name, String team) {
        this.id = id;
        this.name = name;
        this.team = team;
    }
    @Generated(hash = 586692638)
    public User() {
    }
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getTeam() {
        return this.team;
    }
    public void setTeam(String team) {
        this.team = team;
    }

}
