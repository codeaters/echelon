package com.app.innovationweek.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by n188851 on 10-03-2017.
 */


@Entity
public class Rule implements Parcelable{

    @Id
    private String id;

    private String rule;

    private String eventId;

    @Generated(hash = 1028662984)
    public Rule(String id, String rule, String eventId) {
        this.id = id;
        this.rule = rule;
        this.eventId = eventId;
    }

    @Generated(hash = 1416885836)
    public Rule() {
    }

    protected Rule(Parcel in) {
        id = in.readString();
        rule = in.readString();
        eventId = in.readString();
    }

    public static final Creator<Rule> CREATOR = new Creator<Rule>() {
        @Override
        public Rule createFromParcel(Parcel in) {
            return new Rule(in);
        }

        @Override
        public Rule[] newArray(int size) {
            return new Rule[size];
        }
    };

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRule() {
        return this.rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public String getEventId() {
        return this.eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(rule);
        dest.writeString(eventId);
    }
}
