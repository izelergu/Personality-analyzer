package com.analyzer.PersonalityAnalyzer;

import com.analyzer.PersonalityAnalyzer.controller.UserController;
import com.analyzer.PersonalityAnalyzer.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import zemberek.morphology.TurkishMorphology;
import zemberek.normalization.*;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ZemberekConnection {

    User userTweets = new User();

    Path lookupRoot = Paths.get("./data/normalization");
    Path lmFile = Paths.get("./data/lm/lm.2gram.slm");
    TurkishMorphology morphology = TurkishMorphology.createWithDefaults();

    String zemberekCommand = "cmd /c python src/main/zemberek.py ";
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
        
        System.out.println(usr);
        /*TurkishSentenceNormalizer normalizer = null;
        try {
            normalizer = new TurkishSentenceNormalizer(morphology, lookupRoot, lmFile);
        } catch (IOException e) {
            e.printStackTrace();
        }*/


        //zemberek.py
        /*try {
            String line;
            Process p = Runtime.getRuntime().exec(zemberekCommand );
            BufferedReader bri = new BufferedReader
                    (new InputStreamReader(p.getInputStream()));
            while ((line = bri.readLine()) != null) {
                System.out.println(line);
            }
            bri.close();
            p.waitFor();
        } catch (Exception err) {
            err.printStackTrace();
        }*/
    }

        public void findWordgroups(){

            String liwcDeneme = "sen sıfır";
            //liwcApp.py
            try {
                String line;
                Process p = Runtime.getRuntime().exec(liwcAppCommand + liwcDeneme);
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
