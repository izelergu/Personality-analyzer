package com.analyzer.PersonalityAnalyzer;

import com.analyzer.PersonalityAnalyzer.entity.User;
import zemberek.morphology.TurkishMorphology;
import zemberek.morphology.analysis.SingleAnalysis;
import zemberek.morphology.analysis.WordAnalysis;
import zemberek.morphology.lexicon.RootLexicon;
import zemberek.normalization.*;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ZemberekConnection {

    private Path lookupRoot = Paths.get("./data/normalization");
    private Path lmFile = Paths.get("./data/lm/lm.2gram.slm");
    private TurkishSentenceNormalizer normalizer;
    private TurkishMorphology morphology;
    TurkishMorphology analyzer;

    private String userCommand = "cmd /c python src/main/User.py ";
    private String liwcAppCommand = "cmd /c python src/main/liwcApp.py ";
    Logger LOGGER = Logger.getLogger(ZemberekConnection.class.getName());

    public ZemberekConnection() {
        morphology = TurkishMorphology.createWithDefaults();
        analyzer = TurkishMorphology.builder()
                .setLexicon(RootLexicon.DEFAULT)
                .disableCache()
                .build();
        try {
            normalizer = new TurkishSentenceNormalizer(morphology, lookupRoot, lmFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getTweets(String username) {
        // User.py analyze buttonunun click fonksiyonu
        try {
            String line = "";
            Process p = Runtime.getRuntime().exec(userCommand + username);
            System.out.println("value: " + p);
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
        List<String> tweets = new ArrayList<String>();
        tweets.addAll(usr.getTweets());


        String appendedWord = "";
        String[] splitedWords;
        WordAnalysis wa = null;
        for (int i = 0; i < tweets.size(); i++) {
            //LOGGER.info("Original tweet: " + tweets.get(i));
            tweets.get(i).replaceAll("http.*\\s+", ""); // remove all links
            tweets.get(i).replaceAll("@\\w+\\s+", ""); //remove mentions
            tweets.get(i).replaceAll("[^A-Za-z0-9çÇğĞİıöÖüÜşŞ]+", " "); //remove punctuations
            splitedWords = tweets.get(i).split("\\s+"); // split the tweet word by word

            for (int j = 0; j < splitedWords.length; j++) { //Each word of a tweet will normalized and its root will be found.
                wa = analyzer.analyze(splitedWords[j]);
                appendedWord = splitedWords[j];
                // Every result root of a word
                for (SingleAnalysis sa : wa) {
                    appendedWord += "|" + sa.getDictionaryItem().lemma;
                }
                appendedWord += " ";
            }
            tweets.set(i, appendedWord);
        }
        return tweets;
    }

    public void findWordgroups(List<String> tweets) {
        System.out.println("girebilirim.");
        //liwcApp.py
        try {
            String line;
            Process p = Runtime.getRuntime().exec(liwcAppCommand + tweets);
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
