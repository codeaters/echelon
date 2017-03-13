package com.app.innovationweek.model.firebase;

import java.util.Map;

/**
 * Created by zeeshan on 3/13/2017.
 */

public class Event {
    String id;
    String name;
    String description;
    String imageUrl;
    String quizId;
    long startDate;
    Map<String, Phase> phases;
    Map<String, String> rules;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getQuizId() {
        return quizId;
    }

    public void setQuizId(String quizId) {
        this.quizId = quizId;
    }

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public Map<String, Phase> getPhases() {
        return phases;
    }

    public void setPhases(Map<String, Phase> phases) {
        this.phases = phases;
    }

    public Map<String, String> getRules() {
        return rules;
    }

    public void setRules(Map<String, String> rules) {
        this.rules = rules;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", quizId='" + quizId + '\'' +
                ", startDate=" + startDate +
                ", phases=" + phases +
                ", rules=" + rules +
                '}';
    }
}
