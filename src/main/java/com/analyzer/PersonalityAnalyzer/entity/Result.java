package com.analyzer.PersonalityAnalyzer.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "Result")
public class Result {

    @Id
    private String id;
    private String detail_id;
    private String username;
    private String opennes;
    private String extraversion;
    private String neuroticism;
    private String conscientiousness;
    private String aggreeableness;

    public Result(String username, String opennes, String extraversion, String neuroticism, String conscientiousness, String aggreeableness) {
        this.username = username;
        this.opennes = opennes;
        this.extraversion = extraversion;
        this.neuroticism = neuroticism;
        this.conscientiousness = conscientiousness;
        this.aggreeableness = aggreeableness;
    }

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

    public String getDetail_id() {
        return detail_id;
    }

    public void setDetail_id(String detail_id) {
        this.detail_id = detail_id;
    }
}
