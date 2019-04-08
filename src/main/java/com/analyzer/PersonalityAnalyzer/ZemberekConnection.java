package com.analyzer.PersonalityAnalyzer;

import com.analyzer.PersonalityAnalyzer.entity.User;
import com.mongodb.BasicDBObject;


import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import zemberek.morphology.TurkishMorphology;
import zemberek.morphology.analysis.SingleAnalysis;
import zemberek.morphology.analysis.WordAnalysis;
import zemberek.morphology.lexicon.RootLexicon;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ZemberekConnection {

    TurkishMorphology analyzer;
    private String userCommand = "cmd /c python src/main/LIWC/User.py ";
    private String liwcAppCommand = "cmd /c python src/main/LIWC/liwcApp.py ";
    Logger LOGGER = Logger.getLogger(ZemberekConnection.class.getName());

    MongoClientURI uri;
    MongoClient client;
    MongoDatabase db;
    MongoCollection<Document> colUser;

    public ZemberekConnection() {
        analyzer = TurkishMorphology.builder()
                .setLexicon(RootLexicon.DEFAULT)
                .disableCache()
                .build();

        uri = new MongoClientURI("mongodb+srv://ismailyankayis:2430zcbg@twitterpersonalityanalyzer-aeniz.mongodb.net/test?retryWrites=true");
        client = new MongoClient(uri);
        db = client.getDatabase("TwitterPersonalityAnalyzerDB");
        colUser = db.getCollection("User");
    }

    public void getTweets(String username) {
        // User.py analyze buttonunun click fonksiyonu
        LOGGER.info("getTweets function started for " + username);
        try {
            String line = "";
            Process p = Runtime.getRuntime().exec(userCommand + username);
            System.out.println("value: " + p.toString());
            BufferedReader bri = new BufferedReader
                    (new InputStreamReader(p.getInputStream()));
            while ((line = bri.readLine()) != null) {
                System.out.println(line);
            }
            bri.close();
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    public List<String> normalizeTweets(User usr) {
        LOGGER.info("normalizeTweets function started for " + usr.getUsername());
        List<String> tweets = new ArrayList<String>();
        tweets.addAll(usr.getTweets());

        String appendedWord = ""; // Temp String to append analyzed words.
        String[] splitedWords; // list to pick up all optional root of a word
        ArrayList<String> optionsList = new ArrayList<>();
        WordAnalysis wa;
        for (int i = 0; i < tweets.size(); i++) {
            tweets.set(i, tweets.get(i).replaceAll("(http.*\\s*)", "")); // remove all links
            tweets.set(i, tweets.get(i).replaceAll("(@\\w+\\s*)", "")); //remove mentions
            tweets.set(i, tweets.get(i).replaceAll("(\\s+RT\\s+)|(^RT\\s+)", "")); // remove RT tags
            tweets.set(i, tweets.get(i).replaceAll("[^A-Za-z0-9çÇğĞİıöÖüÜşŞ]+", " ")); //remove punctuations
            splitedWords = tweets.get(i).split("\\s+"); // split the tweet word by word

            for (int j = 0; j < splitedWords.length; j++) { //Each word of a tweet will normalized and its root will be found.
                wa = analyzer.analyze(splitedWords[j]);
                optionsList.add(splitedWords[j].toLowerCase());
                // Every result root of a word
                for (SingleAnalysis sa : wa) {
                    if (!optionsList.contains(sa.getDictionaryItem().lemma.toLowerCase()))
                        optionsList.add(sa.getDictionaryItem().lemma.toLowerCase());
                }

                for (int k = 0; k < optionsList.size(); k++) { // optinal roots of words appended to the tweet.
                    if (k > 0)
                        appendedWord += "|";
                    appendedWord += optionsList.get(k);
                }
                optionsList.clear();
                if (!appendedWord.equals(""))
                    appendedWord += " ";
            }
            if (!appendedWord.equals(""))
                tweets.set(i, appendedWord);
            else
                tweets.remove(i--); // remove empty tweets.
            appendedWord = "";
        }
        return tweets;
    }

    public void findWordgroups(User usr) {
        LOGGER.info("findWordGroups function started.");
        //liwcApp.py
        try {
            String line;
            Process p = Runtime.getRuntime().exec(liwcAppCommand + usr.getUsername());
            InputStream error = p.getErrorStream();
            for (int i = 0; i < error.available(); i++) {
                System.out.println("" + error.read());
            }
            BufferedReader bri = new BufferedReader
                    (new InputStreamReader(p.getInputStream()));
            while ((line = bri.readLine()) != null) {
                System.out.println(line);
            }
            bri.close();
            p.waitFor();
        } catch (Exception err) {
            err.printStackTrace();
        }
    }
}
