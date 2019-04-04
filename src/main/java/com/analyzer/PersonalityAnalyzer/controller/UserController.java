package com.analyzer.PersonalityAnalyzer.controller;

import com.analyzer.PersonalityAnalyzer.ZemberekConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import com.analyzer.PersonalityAnalyzer.entity.User;
import com.analyzer.PersonalityAnalyzer.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/User")
public class UserController {

    @Autowired
    UserService userService;

    ZemberekConnection zemberekCon = new ZemberekConnection();

    @RequestMapping(path="/findAll", method = RequestMethod.GET)
    public  @ResponseBody
    List<User> findAll(){
        return userService.findAll();
    }

    @RequestMapping(path="/findUserById/{id}", method = RequestMethod.GET)
    public  @ResponseBody User findUserById(@PathVariable String id){
        return userService.findUserById(id);
    }

    @RequestMapping(path="/findUserByUsername/{username}", method = RequestMethod.GET)
    public @ResponseBody User findUserByUsername(@PathVariable String username){
        return userService.findUserByUsername(username);
    }

    @RequestMapping(path="/analyzeButton/{username}")
    public  @ResponseBody void analyzeButton(@PathVariable String username){
        zemberekCon.getTweets(username);
        User usr = userService.findUserByUsername(username);
        zemberekCon.normalizeTweets(usr);
        zemberekCon.findWordgroups(usr);
    }
}
