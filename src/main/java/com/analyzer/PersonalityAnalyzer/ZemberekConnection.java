package com.analyzer.PersonalityAnalyzer;

import com.analyzer.PersonalityAnalyzer.entity.User;
import zemberek.morphology.TurkishMorphology;
import zemberek.morphology.analysis.SingleAnalysis;
import zemberek.morphology.analysis.WordAnalysis;
import zemberek.morphology.lexicon.RootLexicon;
import zemberek.normalization.TurkishSentenceNormalizer;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.Random;

public class ZemberekConnection {

    Path lookupRoot = Paths.get("data/normalization");
    Path lmFile = Paths.get("data/lm/lm.2gram.slm");
    TurkishMorphology morphology;
    TurkishSentenceNormalizer normalizer;
    TurkishMorphology analyzer;
    private String userCommand = "cmd /c python src/main/LIWC/User.py ";
    private String liwcAppCommand = "cmd /c python src/main/LIWC/liwcApp.py ";
    Logger LOGGER = Logger.getLogger(ZemberekConnection.class.getName());

    public ZemberekConnection() {
        analyzer = TurkishMorphology.builder()
                .setLexicon(RootLexicon.DEFAULT)
                .disableCache()
                .build();
        morphology = TurkishMorphology.createWithDefaults();
        try {
            normalizer = new
                    TurkishSentenceNormalizer(morphology, lookupRoot, lmFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getTweets(String username) {
        // User.py analyze buttonunun click fonksiyonu
        LOGGER.info("getTweets function started for " + username);
        String returnMessage = "";
        try {
            String line = "";
            Process p = Runtime.getRuntime().exec(userCommand + username);
            System.out.println("value: " + p.toString());
            BufferedReader bri = new BufferedReader
                    (new InputStreamReader(p.getInputStream()));
            while ((line = bri.readLine()) != null) {
                if(line.equals("Authorized"))
                    returnMessage = "Başarılı";
                else if(line.equals("NotAuthorized"))
                    returnMessage = "Kullanıcı Profili Gizli!";
                else if(line.equals("UserNotFound"))
                    returnMessage = "Kullanıcı Adı Bulunamadı!";
                else if(line.equals("UnknownError"))
                    returnMessage = "Hata! Lütfen Daha Sonra Tekrar Deneyiniz!";
                System.out.println(line);
            }
            bri.close();
            p.waitFor();
            LOGGER.info("getTweets function finished for " + username);
        } catch (Exception err) {
            returnMessage = "Hata! Lütfen Daha Sonra Tekrar Deneyiniz!";
            err.printStackTrace();
        }
        LOGGER.info(returnMessage);
        return returnMessage;
    }

    public List<Object> normalizeTweets(User usr) {
        LOGGER.info("normalizeTweets function started for " + usr.getUsername());
        List<String> tweets = new ArrayList<String>();
        tweets.addAll(usr.getTweets());

        String appendedWord = ""; // Temp String to append analyzed words.
        String[] splitedWords; // list to pick up all optional root of a word
        ArrayList<String> optionsList = new ArrayList<>();
        WordAnalysis wa;
        int countRT = findRTCount(tweets);
        int countDeletedTweet = 0;
        String tweet = "";
        for (int i = 0; i < tweets.size(); i++) {
            tweet = tweets.get(i);
            tweet = tweet.replaceAll("(http[A-za-z0-9/:.]+\\s*)", ""); // remove all links
            tweet = tweet.replaceAll("(@\\w+\\s*)", ""); //remove mentions
            tweet.replaceAll("(^RT\\s+)", ""); // remove RT tags
            tweet = normalizer.normalize(tweet);
            tweet = tweet.replaceAll("[^A-Za-z0-9çÇğĞİıöÖüÜşŞ]+", " "); //remove punctuations
            tweets.set(i, tweet);
            splitedWords = tweets.get(i).split("\\s+"); // split the tweet word by word

            if(splitedWords.length < 2  && splitedWords[0].equalsIgnoreCase("RT")) { // Tweet has only 1 word or 'RT'
                tweets.remove(i--);
                countDeletedTweet++;
                continue;
            }

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
        LOGGER.info("normalizeTweets function finished for " + usr.getUsername());
        return Arrays.asList(tweets, countDeletedTweet, countRT);
    }

    public void findWordgroups(String username) {
        LOGGER.info("findWordGroups function started for " + username);
        //liwcApp.py
        try {
            String line;
            Process p = Runtime.getRuntime().exec(liwcAppCommand + username);
            BufferedReader bri = new BufferedReader
                    (new InputStreamReader(p.getInputStream()));
            while ((line = bri.readLine()) != null) {
                System.out.println(line);
            }
            bri.close();
            p.waitFor();
            LOGGER.info("findWordGroups function finished for " + username);
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    /**
     * Get User tweets as string list as parameter. Choose random tweets from the list amount of the count.
     * @param list
     * @param count
     * @return
     */
    public List<String> chooseRandomTweet(List<String> list, int count) {
        if(count > list.size()) {
            LOGGER.info("User has not " + count + " tweets, only have " + list.size());
            return null;
        }
        List<String> returnList = new ArrayList<>();
        List<Integer> choosedIndexes = new ArrayList<>() ;
        String tweet = "";
        Random rand = new Random();
        int index = 0;
        for (int i = 0; i < count; i++) {
            index = rand.nextInt(list.size());
            while(choosedIndexes.contains(index)) index = rand.nextInt(list.size());
            tweet = list.get(index);
            tweet = tweet.replaceAll("(http[A-za-z0-9/:.]+\\s*)", ""); // remove all links
            tweet = tweet.replaceAll("(@\\w+\\s*)", ""); //remove mentions
            tweet.replaceAll("(^RT\\s+)", ""); // remove RT tags
            tweet = normalizer.normalize(tweet);
            tweet = tweet.replaceAll("[^A-Za-z0-9çÇğĞİıöÖüÜşŞ]+", " "); //remove punctuations
            if(tweet.split("\\s+").length > 0) {
                choosedIndexes.add(index);
                returnList.add(tweet);
            }
        }
        return  returnList;
    }

    /**
     * Find amount of RT in the user tweets.
     * @param list
     * @return
     */
    public int findRTCount(List<String> list) {
        String[] temp;
        int count = 0;
        for (int i = 0; i < list.size(); i++) {
            temp = list.get(i).split("\\s+");
            if(temp.length > 0 && temp[0].equalsIgnoreCase("RT"))
                count++;
        }

        return count;
    }
}
