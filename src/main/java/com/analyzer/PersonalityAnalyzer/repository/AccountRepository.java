package com.analyzer.PersonalityAnalyzer.repository;

import com.analyzer.PersonalityAnalyzer.entity.Account;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends MongoRepository<Account, String> {


}
