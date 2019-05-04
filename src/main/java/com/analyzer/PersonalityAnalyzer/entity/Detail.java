package com.analyzer.PersonalityAnalyzer.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "Detail")
public class Detail {

    @Id
    private String id;
    private String username;
    private String firstTweetDate;
    private String lastTweetDate;
    private int numberOfTweets;
    private int numberOfRT;
    private int numberofWordsUsed;
    private int numberOfWordsAnalyzed;
    private int numberOfAnalyzedTweets;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getNumberOfTweets() {
        return numberOfTweets;
    }

    public void setNumberOfTweets(int numberOfTweets) {
        this.numberOfTweets = numberOfTweets;
    }

    public int getNumberOfRT() {
        return numberOfRT;
    }

    public String getFirstTweetDate() {
        return firstTweetDate;
    }

    public void setFirstTweetDate(String firstTweetDate) {
        this.firstTweetDate = firstTweetDate;
    }

    public String getLastTweetDate() {
        return lastTweetDate;
    }

    public void setLastTweetDate(String lastTweetDate) {
        this.lastTweetDate = lastTweetDate;
    }

    public void setNumberOfRT(int numberOfRT) {
        this.numberOfRT = numberOfRT;
    }

    public int getNumberofWordsUsed() {
        return numberofWordsUsed;
    }

    public void setNumberofWordsUsed(int numberofWordsUsed) {
        this.numberofWordsUsed = numberofWordsUsed;
    }

    public int getNumberOfWordsAnalyzed() {
        return numberOfWordsAnalyzed;
    }

    public void setNumberOfWordsAnalyzed(int numberOfWordsAnalyzed) {
        this.numberOfWordsAnalyzed = numberOfWordsAnalyzed;
    }

    public int getNumberOfAnalyzedTweets() {
        return numberOfAnalyzedTweets;
    }

    public void setNumberOfAnalyzedTweets(int numberOfAnalyzedTweets) {
        this.numberOfAnalyzedTweets = numberOfAnalyzedTweets;
    }
}
