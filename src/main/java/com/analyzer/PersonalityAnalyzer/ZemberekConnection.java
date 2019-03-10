package com.analyzer.PersonalityAnalyzer;

import com.analyzer.PersonalityAnalyzer.controller.UserController;
import org.springframework.beans.factory.annotation.Autowired;
import zemberek.morphology.TurkishMorphology;
import zemberek.normalization.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ZemberekConnection {

    @Autowired
    UserController userController;

    public void normalize () {

        Path lookupRoot = Paths.get("./data/normalization");
        Path lmFile = Paths.get("./data/lm/lm.2gram.slm");
        TurkishMorphology morphology = TurkishMorphology.createWithDefaults();
        TurkishSentenceNormalizer normalizer = null;
        try {
            normalizer = new TurkishSentenceNormalizer(morphology, lookupRoot, lmFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(normalizer.normalize("Yrn okua gidicem"));
        System.out.println(normalizer.normalize("Çoooook gzl"));

        userController.findAll();
    }
}
