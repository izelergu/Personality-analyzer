package com.analyzer.PersonalityAnalyzer.service;

import com.analyzer.PersonalityAnalyzer.entity.Result;
import com.analyzer.PersonalityAnalyzer.repository.ResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ResultService {

    @Autowired
    ResultRepository resultRepository;

    public Result findResultByUsername(String name){
        return resultRepository.findResultByUsername(name);
    }

    public Result findResultById(String id){
        return resultRepository.findResultById(id);
    }

    public void create (Result result){
        resultRepository.save(result);
    }

    public void update(Result result) {
        resultRepository.save(result);
    }
}
