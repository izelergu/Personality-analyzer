package com.analyzer.PersonalityAnalyzer.controller;

import com.analyzer.PersonalityAnalyzer.ZemberekConnection;
import com.analyzer.PersonalityAnalyzer.entity.Detail;
import com.analyzer.PersonalityAnalyzer.entity.Result;
import com.analyzer.PersonalityAnalyzer.entity.StringResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import com.analyzer.PersonalityAnalyzer.entity.User;
import com.analyzer.PersonalityAnalyzer.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.List;
import java.util.logging.Logger;

@Controller
@RequestMapping("/User")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    DetailController detailController;

    @Autowired
    ResultController resultController;

    ZemberekConnection zemberekCon = new ZemberekConnection();
    Logger LOGGER = Logger.getLogger(UserController.class.getName());
    private String predictCommand = "cmd /c python src/main/LIWC/predictPersonality.py ";

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

    @RequestMapping(path="/analyzeButton/{username}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public StringResponse analyzeButton(@PathVariable String username){
        User usr = null;
        String returnMessage = zemberekCon.getTweets(username);

        if(returnMessage.equals("Başarılı")) {
            usr = userService.findUserByUsername(username);
            List<Object> returnValues = zemberekCon.normalizeTweets(usr);
            List<String> tweets = (List<String>)returnValues.get(0);
            int countDelete = (int)returnValues.get(1);
            int countRT = (int)returnValues.get(2);
            Detail detail = detailController.findDetailByUsername(username);
            detail.setNumberOfRT(countRT);
            detail.setNumberOfAnalyzedTweets(tweets.size());
            detailController.update(detail);
            usr.setPreprocessedTweets(tweets);
            update(usr);
            zemberekCon.findWordgroups(usr.getUsername());
            callPrediction(username);
        }
        return new StringResponse(returnMessage);
    }

    private void callPrediction(String username) {
        LOGGER.info("Prediction function started for " + username);
        //liwcApp.py
        try {
            String line;
            Process p = Runtime.getRuntime().exec(predictCommand + username);
            BufferedReader bri = new BufferedReader
                    (new InputStreamReader(p.getInputStream()));
            while ((line = bri.readLine()) != null) {
                System.out.println(line);
            }
            bri.close();
            p.waitFor();
            LOGGER.info("Prediction function finished for " + username);
        } catch (Exception err) {
            err.printStackTrace();
        }
    }
}
