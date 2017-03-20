package com.app.iw.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by n188851 on 10-03-2017.
 */


@Entity
@IgnoreExtraProperties
public class Rule implements Parcelable {

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
    @Id(autoincrement = true)
    private Long id;
    private String rule;
    private String eventId;
    private long phaseId;

    protected Rule(Parcel in) {
        id = in.readLong();
        rule = in.readString();
        eventId = in.readString();
        phaseId = in.readLong();
    }


    @Generated(hash = 1771873648)
    public Rule(Long id, String rule, String eventId, long phaseId) {
        this.id = id;
        this.rule = rule;
        this.eventId = eventId;
        this.phaseId = phaseId;
    }


    @Generated(hash = 1416885836)
    public Rule() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(rule);
        dest.writeString(eventId);
        dest.writeLong(phaseId);
    }


    public Long getId() {
        return this.id;
    }


    public void setId(Long id) {
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


    public long getPhaseId() {
        return this.phaseId;
    }


    public void setPhaseId(long phaseId) {
        this.phaseId = phaseId;
    }


}
