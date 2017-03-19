package com.app.innovationweek.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Madeyedexter on 08-03-2017.
 */

public class Option implements Parcelable {

    private String value;

    private Boolean correct;

    protected Option(Parcel in) {
        value = in.readString();
        boolean[] tmp = new boolean[1];
        in.readBooleanArray(tmp);
        this.correct = tmp[0];
    }

    public Option(){}

    public static final Creator<Option> CREATOR = new Creator<Option>() {
        @Override
        public Option createFromParcel(Parcel in) {
            return new Option(in);
        }

        @Override
        public Option[] newArray(int size) {
            return new Option[size];
        }
    };

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Boolean getCorrect() {
        return correct;
    }

    public void setCorrect(Boolean correct) {
        this.correct = correct;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(value);
        parcel.writeBooleanArray(new boolean[]{correct});
    }
}
