package com.analyzer.PersonalityAnalyzer.service;

import com.analyzer.PersonalityAnalyzer.entity.Detail;
import com.analyzer.PersonalityAnalyzer.repository.DetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DetailService {

    @Autowired
    DetailRepository detailRepository;

    public void create (Detail detail){
        detailRepository.save(detail);
    }

    public Detail findDetailByUsername(String username){
        return detailRepository.findDetailByUsername(username);
    }

    public Detail findDetailById(String id){
        return detailRepository.findDetailById(id);
    }

    public void update(Detail detail) {
        detailRepository.save(detail);
    }

}
