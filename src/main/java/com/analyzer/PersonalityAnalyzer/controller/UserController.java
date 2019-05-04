package com.analyzer.PersonalityAnalyzer.controller;

import com.analyzer.PersonalityAnalyzer.ZemberekConnection;
import com.analyzer.PersonalityAnalyzer.entity.Detail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import com.analyzer.PersonalityAnalyzer.entity.User;
import com.analyzer.PersonalityAnalyzer.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.Buffer;
import java.util.List;

@Controller
@RequestMapping("/User")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    DetailController detailController;

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
    public  @ResponseBody User analyzeButton(@PathVariable String username){
        User usr = null;
        if(zemberekCon.getTweets(username)) {
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
            zemberekCon.findWordgroups(usr);
        }
        /*try {
            FileReader fr = new FileReader("src/main/LIWC/Data3.csv");
            BufferedReader br = new BufferedReader(fr);
            String line = "";
            String uname = "";
            line = br.readLine();
            while((line = br.readLine()) != null) {
                uname = line.split(",")[1];
                if(zemberekCon.getTweets(uname)) {
                    usr = userService.findUserByUsername(uname);
                    List<String> tweets = zemberekCon.normalizeTweets(usr);
                    usr.setPreprocessedTweets(tweets);
                    update(usr);
                    zemberekCon.findWordgroups(usr);
                }
            }
            fr.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        return usr;
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
