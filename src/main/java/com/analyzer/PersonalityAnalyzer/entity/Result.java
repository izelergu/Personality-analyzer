package com.analyzer.PersonalityAnalyzer.entity;

import org.springframework.data.annotation.Id;

public class Result {

    @Id
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getOpennes() {
        return opennes;
    }

    public void setOpennes(String opennes) {
        this.opennes = opennes;
    }

    public String getExtraversion() {
        return extraversion;
    }

    public void setExtraversion(String extraversion) {
        this.extraversion = extraversion;
    }

    public String getNeuroticism() {
        return neuroticism;
    }

    public void setNeuroticism(String neuroticism) {
        this.neuroticism = neuroticism;
    }

    public String getConscientiousness() {
        return conscientiousness;
    }

    public void setConscientiousness(String conscientiousness) {
        this.conscientiousness = conscientiousness;
    }

    public String getAggreeableness() {
        return aggreeableness;
    }

    public void setAggreeableness(String aggreeableness) {
        this.aggreeableness = aggreeableness;
    }

    private String username;
    private String opennes;
    private String extraversion;
    private String neuroticism;
    private String conscientiousness;
    private String aggreeableness;
}
