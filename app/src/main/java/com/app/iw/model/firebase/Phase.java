package com.app.iw.model.firebase;

import java.util.Map;

/**
 * Created by zeeshan on 3/13/2017.
 */

public class Phase {
    Map<String, String> rules;
    Long startDate;
    String name;
    String leaderboardId;
    String leaderboardType;
    int sortOrder;

    public String getLeaderboardId() {
        return leaderboardId;
    }

    public void setLeaderboardId(String leaderboardId) {
        this.leaderboardId = leaderboardId;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }

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

    public String getLeaderboardType() {
        return leaderboardType;
    }

    public void setLeaderboardType(String leaderboardType) {
        this.leaderboardType = leaderboardType;
    }

    @Override
    public String toString() {
        return "Phase{" +
                "rules=" + rules +
                ", name=" + name +
                ", sortOrder=" + sortOrder +
                ", leaderboardId=" + leaderboardId +
                ", leaderboardType=" + leaderboardType +
                ", startDate=" + startDate +
                '}';
    }
}
