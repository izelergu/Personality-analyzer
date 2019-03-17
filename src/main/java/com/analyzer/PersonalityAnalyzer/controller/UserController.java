package com.analyzer.PersonalityAnalyzer.controller;

import com.analyzer.PersonalityAnalyzer.ZemberekConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import com.analyzer.PersonalityAnalyzer.entity.User;
import com.analyzer.PersonalityAnalyzer.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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

    @RequestMapping(path="/findUserById/{id}", method = RequestMethod.POST)
    public  @ResponseBody HttpStatus update(@RequestBody User usr){
        userService.update(usr);
        return HttpStatus.OK;
    }

    @RequestMapping(path="/findUserByUsername/{username}", method = RequestMethod.GET)
    public  @ResponseBody User findUserByUsername(@PathVariable String username){
        return userService.findUserByUsername(username);
    }

    @RequestMapping(path="/getUserTweets/{username}")
    public void getUserTweets (@PathVariable String username)throws IOException {
        String tweetsFile = "normalizedWords.txt";
        zemberekCon.getTweets(username);
        //zemberekCon.normalizeTweets(tweetsFile);
        //zemberekCon.findWordgroups();
    }

}
