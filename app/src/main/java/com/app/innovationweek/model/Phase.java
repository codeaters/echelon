package com.app.innovationweek.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;

import java.util.List;

/**
 * Created by zeeshan on 3/13/2017.
 */
@Entity
public class Phase {
    @Id
    private long id;
    private String eventId;
    @ToOne()
    List<Rule> rules;
    private long startDate;

}
