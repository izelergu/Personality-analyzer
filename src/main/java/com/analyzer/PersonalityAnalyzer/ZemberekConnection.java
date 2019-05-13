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

    private final int NUMBER_OF_TWEET = 25;

    private Path lookupRoot = Paths.get("data/normalization");
    private Path lmFile = Paths.get("data/lm/lm.2gram.slm");
    private TurkishMorphology morphology;
    private TurkishSentenceNormalizer normalizer;
    private TurkishMorphology analyzer;
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

    public List<Object> getTweets(String username) {
        // User.py analyze buttonunun click fonksiyonu
        LOGGER.info("getTweets function started for " + username);
        String returnMessage = "";
        String detail_id = "";
        try {
            String line = "";
            Process p = Runtime.getRuntime().exec(userCommand + username);
            System.out.println("value: " + p.toString());
            BufferedReader bri = new BufferedReader
                    (new InputStreamReader(p.getInputStream()));
            while ((line = bri.readLine()) != null) {
                if (line.equals("Authorized"))
                    returnMessage = "Başarılı";
                else if (line.equals("NotAuthorized"))
                    returnMessage = "Kullanıcı Profili Gizli!";
                else if (line.equals("UserNotFound"))
                    returnMessage = "Kullanıcı Adı Bulunamadı!";
                else if (line.equals("UnknownError"))
                    returnMessage = "Hata! Lütfen Daha Sonra Tekrar Deneyiniz!";
                else if (line.contains("detail_id:"))
                    detail_id = line.split(":")[1];
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
        return Arrays.asList(returnMessage, detail_id);
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
        //tweets = chooseRandomTweet(tweets, NUMBER_OF_TWEET);
        tweets = chooseInOrderTwets(tweets, NUMBER_OF_TWEET);
        String tweet = "";
        for (int i = 0; i < tweets.size(); i++) {
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
        LOGGER.info("normalizeTweets function finished for " + usr.getUsername());
        return Arrays.asList(tweets, countDeletedTweet, countRT);
    }

    public void findWordgroups(String username, String detail_id) {
        LOGGER.info("findWordGroups function started for " + username);
        //liwcApp.py
        try {
            String line;
            Process p = Runtime.getRuntime().exec(liwcAppCommand + username + " " + detail_id);
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
     *
     * @param list
     * @param count
     * @return
     */
    private List<String> chooseRandomTweet(List<String> list, int count) {

        List<String> returnList = new ArrayList<>();
        List<Integer> choosedIndexes = new ArrayList<>();
        int countDeletedTweet = 0;
        Random rand = new Random();
        int index = 0;
        String tweet = "";

        if (count > list.size()) {
            LOGGER.info("User has not " + count + " tweets, only have " + list.size());
            for (int i = 0; i < list.size(); i++) {
                tweet = list.get(i);
                tweet = normalizeSingleTweet(tweet);
                if (tweet.split("\\s+").length > 0 && !tweet.equalsIgnoreCase("RT") && !tweet.equals("")) {
                    returnList.add(tweet);
                } else {
                    countDeletedTweet++;
                }
            }
            LOGGER.info(String.format("%d tweets added and %d tweets deleted", returnList.size(), countDeletedTweet));
            return list;
        }

        for (int i = 0; i < count; i++) {
            if (countDeletedTweet + returnList.size() == list.size()) break;
            index = rand.nextInt(list.size());
            while (choosedIndexes.contains(index)) index = rand.nextInt(list.size());
            try {
                tweet = list.get(index);
                tweet = normalizeSingleTweet(tweet);
                if (tweet.split("\\s+").length > 0 && !tweet.equalsIgnoreCase("RT")) {
                    choosedIndexes.add(index);
                    returnList.add(tweet);
                } else {
                    countDeletedTweet++;
                    i--;
                }
            } catch (Exception e) {
                countDeletedTweet++;
                i--;
            }
        }
        LOGGER.info(String.format("%d tweets added and %d tweets deleted", returnList.size(), countDeletedTweet));
        return returnList;
    }

    private List<String> chooseInOrderTwets(List<String> list, int count) {
        List<String> returnList = new ArrayList<>();
        int countDeletedTweet = 0;
        String tweet = "";
        if (count > list.size()) {
            LOGGER.info("User has not " + count + " tweets, only have " + list.size());
            for (int i = 0; i < list.size(); i++) {
                try {
                    tweet = list.get(i);
                    tweet = normalizeSingleTweet(tweet);
                    if (tweet.split("\\s+").length > 0 && !tweet.equalsIgnoreCase("RT")) {
                        returnList.add(tweet);
                    } else {
                        countDeletedTweet++;
                        continue;
                    }
                } catch (Exception e) {
                    countDeletedTweet++;
                    continue;
                }
            }
        }

        for (int i = 0; i < count; i++) {
            try {
                tweet = list.get(i);
                tweet = normalizeSingleTweet(tweet);
                if (tweet.split("\\s+").length > 0 && !tweet.equalsIgnoreCase("RT")) {
                    returnList.add(tweet);
                } else {
                    countDeletedTweet++;
                    i--;
                }
            } catch (Exception e) {
                countDeletedTweet++;
                i--;
            }
        }
        LOGGER.info(String.format("%d tweets added and %d tweets deleted", returnList.size(), countDeletedTweet));
        return returnList;
    }

    /**
     * Removes unneccesary links, mentions, words, and punctuations from given tweet.
     *
     * @param tweet
     * @return
     */
    private String normalizeSingleTweet(String tweet) {
        tweet = tweet.replaceAll("(http[A-za-z0-9/:.]+\\s*)", ""); // remove all links
        tweet = tweet.replaceAll("(@\\w+:\\s*)", ""); //remove mentions
        tweet = tweet.replaceAll("^RT\\s+", ""); // remove RT tags
        tweet = tweet.replaceAll("[^A-Za-z0-9çÇğĞİıöÖüÜşŞ]+", " "); //remove punctuations
        if (tweet.equals("") || tweet.replaceAll("\\s+", "").equals("")) return "";
        tweet = normalizer.normalize(tweet);
        return tweet;
    }

    /**
     * Find amount of RT in the user tweets.
     *
     * @param list
     * @return
     */
    private int findRTCount(List<String> list) {
        String[] temp;
        int count = 0;
        for (int i = 0; i < list.size(); i++) {
            temp = list.get(i).split("\\s+");
            if (temp.length > 0 && temp[0].equalsIgnoreCase("RT"))
                count++;
        }

        return count;
    }
}
