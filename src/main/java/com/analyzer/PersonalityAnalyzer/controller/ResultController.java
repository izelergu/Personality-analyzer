package com.analyzer.PersonalityAnalyzer.controller;

import com.analyzer.PersonalityAnalyzer.entity.Result;
import com.analyzer.PersonalityAnalyzer.service.ResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/Result")
public class ResultController {

    @Autowired
    ResultService resultService;

    @RequestMapping(path="/getResult/{username}", method = RequestMethod.GET)
    public @ResponseBody
    Result findResultByUsername (@PathVariable String username){
        return resultService.findResultByUsername(username);
    }

    @RequestMapping(path="/getResultById/{id}", method = RequestMethod.GET)
    public @ResponseBody
    Result findResultById (@PathVariable String id){
        return resultService.findResultById(id);
    }

    @RequestMapping(path = "/Create", method = RequestMethod.POST, consumes = "application/json")
    public @ResponseBody
    HttpStatus create(@RequestBody Result result){
        resultService.create(result);
        return HttpStatus.OK;
    }

    @RequestMapping(path="/update", method = RequestMethod.POST, consumes = "application/json")
    public @ResponseBody
    HttpStatus update (@RequestBody Result result){
        resultService.update(result);
        return HttpStatus.OK;
    }

}
