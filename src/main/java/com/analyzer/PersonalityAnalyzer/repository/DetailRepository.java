package com.analyzer.PersonalityAnalyzer.repository;

import com.analyzer.PersonalityAnalyzer.entity.Detail;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetailRepository extends MongoRepository <Detail, String>{

    Detail findDetailByUsername(String username);
    Detail findDetailById(String id);

}
