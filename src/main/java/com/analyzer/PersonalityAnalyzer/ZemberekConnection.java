package com.analyzer.PersonalityAnalyzer;

import com.analyzer.PersonalityAnalyzer.controller.UserController;
import com.analyzer.PersonalityAnalyzer.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import zemberek.morphology.TurkishMorphology;
import zemberek.normalization.*;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ZemberekConnection {

    Path lookupRoot = Paths.get("./data/normalization");
    Path lmFile = Paths.get("./data/lm/lm.2gram.slm");
    TurkishMorphology morphology = TurkishMorphology.createWithDefaults();

    String userCommand = "cmd /c python src/main/User.py ";
    String liwcAppCommand = "cmd /c python src/main/liwcApp.py ";

    public void getTweets(String username){
        // User.py analyze buttonunun click fonksiyonu
        try {
            String line = "";
            Process p = Runtime.getRuntime().exec(userCommand + username);
            System.out.println("value: "+p);
            BufferedReader bri = new BufferedReader
                    (new InputStreamReader(p.getInputStream()));
            while ((line = bri.readLine()) != null) {
                System.out.println(line);
            }
            bri.close();
        }
        catch (Exception err) {
            err.printStackTrace();
        }
    }

    public void normalizeTweets (User usr){

        TurkishSentenceNormalizer normalizer = null;
        List<String> tweets = new ArrayList<String>();
        tweets.addAll(usr.getTweets());

        for (int i = 0; i < tweets.size() ; i++) {
            try {
                normalizer = new TurkishSentenceNormalizer(morphology, lookupRoot, lmFile);
                String normalized = normalizer.normalize((tweets.get(i)).toString());
                System.out.println(tweets.get(i) + " --> " + normalized);
                tweets.set(i, normalized);//burda normalized olanları liste ekliyoruz
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //zemberek.py
        //findWordgroups(tweets);
    }

        public void findWordgroups(List <String> tweets){

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
