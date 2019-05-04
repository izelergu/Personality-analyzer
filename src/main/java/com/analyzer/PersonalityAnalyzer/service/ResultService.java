package com.analyzer.PersonalityAnalyzer.service;

import com.analyzer.PersonalityAnalyzer.entity.Result;
import com.analyzer.PersonalityAnalyzer.repository.ResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ResultService {

    @Autowired
    ResultRepository resultRepository;

    public Result findResultByUsername(String username){
        return resultRepository.findResultByUsername(username);
    }

    public void create (Result result){
        resultRepository.save(result);
    }
}
