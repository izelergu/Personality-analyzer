package com.analyzer.PersonalityAnalyzer;

import com.analyzer.PersonalityAnalyzer.entity.User;
import zemberek.morphology.TurkishMorphology;
import zemberek.morphology.analysis.SingleAnalysis;
import zemberek.morphology.analysis.WordAnalysis;
import zemberek.morphology.lexicon.RootLexicon;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class ZemberekConnection {

    TurkishMorphology analyzer;
    private String userCommand = "cmd /c python src/main/LIWC/User.py ";
    private String liwcAppCommand = "cmd /c python src/main/LIWC/liwcApp.py ";
    Logger LOGGER = Logger.getLogger(ZemberekConnection.class.getName());

    public ZemberekConnection() {
        analyzer = TurkishMorphology.builder()
                .setLexicon(RootLexicon.DEFAULT)
                .disableCache()
                .build();

    }

    public boolean getTweets(String username) {
        // User.py analyze buttonunun click fonksiyonu
        LOGGER.info("getTweets function started for " + username);
        boolean isAuthorized = false;
        try {
            String line = "";
            Process p = Runtime.getRuntime().exec(userCommand + username);
            System.out.println("value: " + p.toString());
            BufferedReader bri = new BufferedReader
                    (new InputStreamReader(p.getInputStream()));
            while ((line = bri.readLine()) != null) {
                if(line.equals("Authorized"))
                    isAuthorized = true;
                else if(line.equals("NotAuthorized"))
                    isAuthorized = false;
                System.out.println(line);
            }
            bri.close();
            p.waitFor();
            LOGGER.info("getTweets function finished for " + username);
        } catch (Exception err) {
            isAuthorized = false;
            err.printStackTrace();
        }

        return isAuthorized;
    }

    public List<Object> normalizeTweets(User usr) {
        LOGGER.info("normalizeTweets function started for " + usr.getUsername());
        List<String> tweets = new ArrayList<String>();
        tweets.addAll(usr.getTweets());

        String appendedWord = ""; // Temp String to append analyzed words.
        String[] splitedWords; // list to pick up all optional root of a word
        ArrayList<String> optionsList = new ArrayList<>();
        WordAnalysis wa;
        int countRT = 0;
        int countDeletedTweet = 0;
        String tweet = "";
        for (int i = 0; i < tweets.size(); i++) {
            tweet = tweets.get(i);
            tweet = tweet.replaceAll("(http.*\\s*)", ""); // remove all links
            tweet = tweet.replaceAll("(@\\w+\\s*)", ""); //remove mentions
            //tweet.replaceAll("(\\s+RT\\s+)|(^RT\\s+)", ""); // remove RT tags
            tweet = tweet.replaceAll("[^A-Za-z0-9çÇğĞİıöÖüÜşŞ]+", " "); //remove punctuations
            tweets.set(i, tweet);
            splitedWords = tweets.get(i).split("\\s+"); // split the tweet word by word

            if(splitedWords.length < 2  && splitedWords[0].equals("RT")) { // Tweet has only 1 word or 'RT'
                tweets.remove(i--);
                countDeletedTweet++;
                continue;
            }
            if(splitedWords[0].equals("RT")) countRT++;

            for (int j = 1; j < splitedWords.length; j++) { //Each word of a tweet will normalized and its root will be found.
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
        LOGGER.info("normalizeTweets function finished for " + usr.getUsername());
        return Arrays.asList(tweets, countDeletedTweet, countRT);
    }

    public void findWordgroups(User usr) {
        LOGGER.info("findWordGroups function started for " + usr.getUsername());
        //liwcApp.py
        try {
            String line;
            Process p = Runtime.getRuntime().exec(liwcAppCommand + usr.getUsername());
            InputStream error = p.getErrorStream();
            BufferedReader bri = new BufferedReader
                    (new InputStreamReader(p.getInputStream()));
            while ((line = bri.readLine()) != null) {
                System.out.println(line);
            }
            /*for (int i = 0; i < error.available(); i++) {
                System.out.println("" + error.read());
            }*/
            bri.close();
            p.waitFor();
            LOGGER.info("findWordGroups function finished for " + usr.getUsername());
        } catch (Exception err) {
            err.printStackTrace();
        }
    }
}
