package com.app.innovationweek.model.firebase;

import java.util.Map;

/**
 * Created by zeeshan on 3/13/2017.
 */

public class Phase {
    Map<String, String> rules;
    Long startDate;
    String name;

    public Map<String, String> getRules() {
        return rules;
    }

    public void setRules(Map<String, String> rules) {
        this.rules = rules;
    }

    public Long getStartDate() {
        return startDate;
    }

    public void setStartDate(Long startDate) {
        this.startDate = startDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Phase{" +
                "rules=" + rules +
                ", name=" + name +
                ", startDate=" + startDate +
                '}';
    }
}
