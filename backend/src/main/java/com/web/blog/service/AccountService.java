package com.web.blog.service;


import com.web.blog.model.account.Account;
import com.web.blog.model.account.AccountRepository;
import com.web.blog.model.account.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AccountService implements UserDetailsService {
  @Autowired
  AccountRepository accountRepository;
  @Autowired
  PasswordEncoder passwordEncoder;

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
    return new UserAccount(account);

  }

}
