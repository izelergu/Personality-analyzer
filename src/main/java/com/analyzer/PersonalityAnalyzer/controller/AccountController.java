package com.analyzer.PersonalityAnalyzer.controller;

import com.analyzer.PersonalityAnalyzer.entity.Account;
import com.analyzer.PersonalityAnalyzer.entity.StringResponse;
import com.analyzer.PersonalityAnalyzer.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/account")
public class AccountController {
    @Autowired
    AccountService accountService;


    @Bean
    public PasswordEncoder customPasswordEncoder() {
        return new PasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                return BCrypt.hashpw(rawPassword.toString(), BCrypt.gensalt(4));
            }
            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                return BCrypt.checkpw(rawPassword.toString(), encodedPassword);
            }
        };
    }

    @RequestMapping(path = "/create", method = RequestMethod.POST)
    public @ResponseBody HttpStatus create(@RequestBody Account account){
        account.setPassword(BCrypt.hashpw(account.getPassword(),BCrypt.gensalt(4)));
        accountService.create(account);
        return HttpStatus.OK;
    }

    @RequestMapping(path="/findAccountByUsername/{username}", method = RequestMethod.GET)
    public @ResponseBody Account findAccountByUsername (@PathVariable String username){
        return accountService.findAccountByUsername(username);
    }

    @RequestMapping(path="/checkPassword/{password}/{username}", method = RequestMethod.GET)
    public @ResponseBody
    StringResponse checkPassword (@PathVariable String password, @PathVariable String username){
        boolean flag;
        Account account = findAccountByUsername(username);
        if(account == null)  return new StringResponse("false");
        flag = BCrypt.checkpw(password, account.getPassword());
        if(flag)
            return new StringResponse("true");
        else
            return new StringResponse("false");
    }

    @RequestMapping(path="/findAll", method = RequestMethod.GET)
    public @ResponseBody List <Account> findAll (){
        return accountService.findAll();
    }

    @RequestMapping(path = "/login/{username}", method = RequestMethod.GET)
    public @ResponseBody Account login (@PathVariable String username){
        return accountService.login(username);
    }

    @RequestMapping(path="/update", method = RequestMethod.POST, consumes = "application/json")
    public @ResponseBody
    HttpStatus update (@RequestBody Account acc){
        accountService.update(acc);
        return HttpStatus.OK;
    }

}
