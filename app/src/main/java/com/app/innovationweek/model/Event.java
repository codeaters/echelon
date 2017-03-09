package com.app.innovationweek.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;


/**
 * Created by zeeshan on 3/8/2017.
 */
@Entity
public class Event {
    @Id
    private String id;
    private String imageUrl;
    private String name;
    private String description;
    private long startDate;
    private String rule;
    @Generated(hash = 831176215)
    public Event(String id, String imageUrl, String name, String description,
            long startDate, String rule) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.rule = rule;
    }
    @Generated(hash = 344677835)
    public Event() {
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
    public String getRule() {
        return this.rule;
    }
    public void setRule(String rule) {
        this.rule = rule;
    }

}
