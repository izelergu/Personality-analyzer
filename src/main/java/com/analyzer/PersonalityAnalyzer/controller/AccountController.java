package com.analyzer.PersonalityAnalyzer.controller;

import com.analyzer.PersonalityAnalyzer.entity.Account;
import com.analyzer.PersonalityAnalyzer.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/account")
public class AccountController {
    @Autowired
    AccountService accountService;

    @RequestMapping(path = "/create", method = RequestMethod.POST)
    public @ResponseBody HttpStatus create(@RequestBody Account account){
        accountService.create(account);
        return HttpStatus.OK;
    }

    @RequestMapping(path = "/login", method = RequestMethod.GET)
    public @ResponseBody List<Account> login (){
        return accountService.login();
    }

}
