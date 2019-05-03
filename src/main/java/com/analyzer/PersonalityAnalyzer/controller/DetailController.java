package com.analyzer.PersonalityAnalyzer.controller;

import com.analyzer.PersonalityAnalyzer.ZemberekConnection;
import com.analyzer.PersonalityAnalyzer.entity.Detail;
import com.analyzer.PersonalityAnalyzer.service.DetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/Detail")
public class DetailController {

    @Autowired
    DetailService detailService;

    ZemberekConnection zemberekCon = new ZemberekConnection();
    @RequestMapping(path="/getAllDetails", method = RequestMethod.POST, consumes = "application/json")
    public @ResponseBody
    void getAllDetails (@PathVariable String username){
        zemberekCon.getTweets(username);
        //detailService.getAllDetails(username);
    }

    @RequestMapping(path = "/Create", method = RequestMethod.POST, consumes = "application/json")
    public @ResponseBody HttpStatus create(@RequestBody Detail detail){
        detailService.create(detail);
        return HttpStatus.OK;

    }

}
