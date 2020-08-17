package com.web.blog.service.account;


import com.web.blog.exception.account.DuplicatedEmailException;
import com.web.blog.exception.account.DuplicatedNicknameException;
import com.web.blog.model.account.Account;
import com.web.blog.model.account.AccountDetails;
import com.web.blog.model.account.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class AccountService implements UserDetailsService {
  @Autowired
  AccountRepository accountRepository;
  @Autowired
  BCryptPasswordEncoder passwordEncoder;


  public Account createNew(Account account){
    if(accountRepository.findByUseremail(account.getUseremail()) != null){
      throw new DuplicatedEmailException(account.getUseremail());
    } else if(accountRepository.findByUsername(account.getUsername()) != null){
      throw new DuplicatedNicknameException(account.getUsername());
    }
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

  public boolean checkPassword(String str1, String str2, String username){
    Account account = accountRepository.findByUsername(username);

    System.out.println(username);
    System.out.println("DDD" + str1 + " : " +account.getPassword());


    if(!passwordEncoder.matches(str1, account.getPassword())){
      return false;
    } else {
      account.setPassword(str2);
      account.encodePassword(passwordEncoder);
      this.accountRepository.save(account);
    }

    return true;
  }
}
