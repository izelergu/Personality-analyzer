package com.analyzer.PersonalityAnalyzer.controller;

import com.analyzer.PersonalityAnalyzer.ZemberekConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @RequestMapping(path="/update", method = RequestMethod.POST, consumes = "application/json")
    public @ResponseBody
    HttpStatus update (@RequestBody User usr){
        userService.update(usr);
        return HttpStatus.OK;
    }

    @RequestMapping(path="/analyzeButton/{username}")
    public  @ResponseBody void analyzeButton(@PathVariable String username){
        zemberekCon.getTweets(username);
        User usr = userService.findUserByUsername(username);
        List<String> tweets = zemberekCon.normalizeTweets(usr);
        usr.setPreprocessedTweets(tweets);
        update(usr);
        zemberekCon.findWordgroups(usr);
        /*List<User> list  = userService.findAll();
        for (int i = 0; i < list.size(); i++) {
            zemberekCon.getTweets(list.get(i).getUsername());
            list.set(i, userService.findUserByUsername(list.get(i).getUsername()));
            List<String> tweets = zemberekCon.normalizeTweets(list.get(i));
            list.get(i).setPreprocessedTweets(tweets);
            update(list.get(i));
            zemberekCon.findWordgroups(list.get(i));
        }*/
    }
}
