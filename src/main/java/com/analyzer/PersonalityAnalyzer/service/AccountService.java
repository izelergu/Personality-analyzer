package com.analyzer.PersonalityAnalyzer.service;

import com.analyzer.PersonalityAnalyzer.entity.Account;
import com.analyzer.PersonalityAnalyzer.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {

    @Autowired
    AccountRepository accountRepository;

    public void create(Account account){
        accountRepository.save(account);
    }

    public List <Account> login(){
        return accountRepository.findAll();
    }

    public Account findAccountByUsername(String username){
        return accountRepository.findAccountByUsername(username);
    }

    public void update(Account acc) {
        accountRepository.save(acc);
    }
}
