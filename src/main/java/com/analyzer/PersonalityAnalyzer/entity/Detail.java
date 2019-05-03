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
    private int numberOfTweets;
    private int numberOfRT;
    private Date firstDate;
    private Date lastDate;
    private int numberofWordsUsed;
    private int numberOfWordsAnalyzed;

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

    public void setNumberOfRT(int numberOfRT) {
        this.numberOfRT = numberOfRT;
    }

    public Date getFirstDate() {
        return firstDate;
    }

    public void setFirstDate(Date firstDate) {
        this.firstDate = firstDate;
    }

    public Date getLastDate() {
        return lastDate;
    }

    public void setLastDate(Date lastDate) {
        this.lastDate = lastDate;
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
}
