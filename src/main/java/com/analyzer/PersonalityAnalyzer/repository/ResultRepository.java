package com.analyzer.PersonalityAnalyzer.repository;

import com.analyzer.PersonalityAnalyzer.entity.Result;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResultRepository extends MongoRepository <Result, String>{



}
