package com.analyzer.PersonalityAnalyzer.repository;

import com.analyzer.PersonalityAnalyzer.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    List<User> findAll();

    User findUserById(String id);

    User findUserByUsername(String username);
}