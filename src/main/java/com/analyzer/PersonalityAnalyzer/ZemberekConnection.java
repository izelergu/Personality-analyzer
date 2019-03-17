package com.analyzer.PersonalityAnalyzer;

import com.analyzer.PersonalityAnalyzer.entity.User;
import zemberek.morphology.TurkishMorphology;
import zemberek.morphology.analysis.InformalAnalysisConverter;
import zemberek.morphology.analysis.SentenceAnalysis;
import zemberek.morphology.analysis.SingleAnalysis;
import zemberek.morphology.analysis.WordAnalysis;
import zemberek.morphology.lexicon.DictionaryItem;
import zemberek.normalization.*;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ZemberekConnection {

    private Path lookupRoot = Paths.get("./data/normalization");
    private Path lmFile = Paths.get("./data/lm/lm.2gram.slm");
    private TurkishSentenceNormalizer normalizer;
    private TurkishMorphology morphology;

    private String userCommand = "cmd /c python src/main/User.py ";
    private String liwcAppCommand = "cmd /c python src/main/liwcApp.py ";

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

    public void normalizeTweets(User usr) {

        List<String> tweets = new ArrayList<String>();
        tweets.addAll(usr.getTweets());

        List<SingleAnalysis> analyses;
        InformalAnalysisConverter converter = new InformalAnalysisConverter(morphology.getWordGenerator());
        String normalized = "";
        for (int i = 0; i < tweets.size(); i++) {
            analyses = morphology
                    .analyzeAndDisambiguate(tweets.get(i))
                    .bestAnalysis();

            normalized = parseRoot(analyses);
            System.out.println(normalized);
            /*
            normalized = normalizer.normalize((tweets.get(i)));
            System.out.println(tweets.get(i) + " --> " + normalized);
            tweets.set(i, normalized);//burda normalized olanları liste ekliyoruz*/
        }
        //zemberek.py
        //findWordgroups(tweets);
    }

    public String parseRoot(List<SingleAnalysis> analyses) {
        String normalized = "";
        for (SingleAnalysis a : analyses) {
            DictionaryItem item = a.getDictionaryItem();
            if (item.primaryPos.getStringForm().equalsIgnoreCase("Mention") || item.secondaryPos.getStringForm().equalsIgnoreCase("Mention")) {

            } else if (item.primaryPos.getStringForm().equalsIgnoreCase("URL") || item.secondaryPos.getStringForm().equalsIgnoreCase("URL")) {

            } else if (item.primaryPos.toString().equalsIgnoreCase("Punctuation") || item.secondaryPos.toString().equalsIgnoreCase("Punctuation")) {

            } else {
                for (int j = 0; j < a.getMorphemeDataList().size(); j++) {
                    if (a.getMorphemeDataList().get(j).surface.matches(".*:.*")) {
                        // ilki kök yada ek, ikincisi türü.
                        normalized += " " + a.getMorphemeDataList().get(j).surface.split(":")[0];
                    }
                }
            }
        }
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
