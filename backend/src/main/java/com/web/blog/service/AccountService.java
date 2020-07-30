package com.web.blog.service;


import com.web.blog.model.account.Account;
import com.web.blog.model.account.AccountDetails;
import com.web.blog.model.account.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

@Service
public class AccountService implements UserDetailsService {
  @Autowired
  AccountRepository accountRepository;
  @Autowired
  BCryptPasswordEncoder passwordEncoder;


  public Account createNew(Account account){
    account.encodePassword(passwordEncoder);
    return this.accountRepository.save(account);
  }

  @Override
  public UserDetails loadUserByUsername(String useremail) throws UsernameNotFoundException {
    Account account = accountRepository.findByUseremail(useremail);
    if (account == null) {
      throw new UsernameNotFoundException(useremail);
    }
    AccountDetails accountDetails = new AccountDetails();
    accountDetails.setAccount(account);
    accountDetails.setAuthorities(Collections.singleton(new SimpleGrantedAuthority(account.getGrade().getValue())));
    return accountDetails;
  }

}
