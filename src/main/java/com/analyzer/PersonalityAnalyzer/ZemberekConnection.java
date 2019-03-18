package com.analyzer.PersonalityAnalyzer;

import com.analyzer.PersonalityAnalyzer.entity.User;
import zemberek.morphology.TurkishMorphology;
import zemberek.morphology.analysis.InformalAnalysisConverter;
import zemberek.morphology.analysis.SingleAnalysis;
import zemberek.morphology.lexicon.DictionaryItem;
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

    private String userCommand = "cmd /c python src/main/User.py ";
    private String liwcAppCommand = "cmd /c python src/main/liwcApp.py ";
    Logger LOGGER = Logger.getLogger(ZemberekConnection.class.getName());

    public ZemberekConnection() {
        morphology = TurkishMorphology.createWithDefaults();
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

        List<SingleAnalysis> analyses;
        String normalized = "";
        for (int i = 0; i < tweets.size(); i++) {
            LOGGER.info("Original tweet: " + tweets.get(i));
            analyses = morphology
                    .analyzeAndDisambiguate(tweets.get(i))
                    .bestAnalysis();

            normalized = parseRoot(analyses);
            tweets.set(i, normalized);
        }
        return tweets;
    }

    public String parseRoot(List<SingleAnalysis> analyses) {
        String normalized = "";
        String word = "";
        String wordRoot = "";
        String previousType = "";
        for (SingleAnalysis a : analyses) {
            DictionaryItem item = a.getDictionaryItem();
            if (item.primaryPos.getStringForm().equalsIgnoreCase("Mention") || item.secondaryPos.getStringForm().equalsIgnoreCase("Mention")) {
                LOGGER.info(item.lemma + " is mention");
            } else if (item.primaryPos.getStringForm().equalsIgnoreCase("URL") || item.secondaryPos.getStringForm().equalsIgnoreCase("URL")) {
                LOGGER.info(item.lemma + " is URL");
            } else if (item.primaryPos.toString().equalsIgnoreCase("Punctuation") || item.secondaryPos.toString().equalsIgnoreCase("Punctuation")) {
                //LOGGER.info(item.lemma + " is Punctuation");
            } else {
                word = item.lemma;
                wordRoot = item.root;
                try {
                    previousType = a.getMorphemeDataList().get(0).morpheme.name;
                } catch (Exception e) {
                    LOGGER.info(item.lemma + " has no morpheme!");
                }
                for (int j = 1; j < a.getMorphemeDataList().size(); j++) {
                    if (!a.getMorphemeDataList().get(j).surface.equals("")) {
                        if (!a.getMorphemeDataList().get(j).morpheme.derivational)
                            word += " " + a.getMorphemeDataList().get(j).morpheme.name;
                        else {
                            if (a.getMorphemeDataList().get(j).morpheme.name.equalsIgnoreCase("with")
                                    || a.getMorphemeDataList().get(j).morpheme.name.equalsIgnoreCase("without"))
                                word += a.getMorphemeDataList().get(j).surface;
                            else {
                                if (a.getMorphemeDataList().size() > j + 1 && a.getMorphemeDataList().get(j + 1).morpheme.name.equalsIgnoreCase(previousType))
                                    word = wordRoot + a.getMorphemeDataList().get(j).surface;
                            }
                        }
                    }
                }

                normalized += " " + word;
            }
        }
        LOGGER.info("Parsed tweet: " + normalized);
        return normalized;
    }

    public void findWordgroups(List<String> tweets) {

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
