package com.analyzer.PersonalityAnalyzer.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "User")
public class User {

    @Id
    private String id;
    private String username;
    private String last_analysis;
    private List tweets;

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

    public String getLast_analysis() {
        return last_analysis;
    }

    public void setLast_analysis(String last_analysis) {
        this.last_analysis = last_analysis;
    }

    public List getTweets() {
        return tweets;
    }

    public void setTweets(List tweets) {
        this.tweets = tweets;
    }
}
