package com.analyzer.PersonalityAnalyzer.service;

import com.analyzer.PersonalityAnalyzer.entity.User;
import com.analyzer.PersonalityAnalyzer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public List<User> findAll(){
        return userRepository.findAll();
    }

    public User findUserById(String id)
    {
        return userRepository.findUserById(id);
    }

    public User findUserByUsername(String username)
    {
        return userRepository.findUserByUsername(username);
    }

    public void update(User user){
        userRepository.save(user);
    }
}
