package com.analyzer.PersonalityAnalyzer.controller;

import com.analyzer.PersonalityAnalyzer.entity.Detail;
import com.analyzer.PersonalityAnalyzer.service.DetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/Detail")
public class DetailController {

    @Autowired
    DetailService detailService;

    @RequestMapping(path="/getDetails/{username}", method = RequestMethod.GET)
    public @ResponseBody
    List<Detail> findDetailByUsername (@PathVariable String username){
        return detailService.findDetailByUsername(username);
    }

    @RequestMapping(path="/getDetailsById/{id}", method = RequestMethod.GET)
    public @ResponseBody Detail findDetailById (@PathVariable String id){
        return detailService.findDetailById(id);
    }

    @RequestMapping(path = "/Create", method = RequestMethod.POST, consumes = "application/json")
    public @ResponseBody HttpStatus create(@RequestBody Detail detail){
        if(findDetailByUsername(detail.getUsername()) == null)
            detailService.create(detail);
        return HttpStatus.OK;
    }

    @RequestMapping(path="/update", method = RequestMethod.POST, consumes = "application/json")
    public @ResponseBody
    HttpStatus update (@RequestBody Detail detail){
        detailService.update(detail);
        return HttpStatus.OK;
    }

}
