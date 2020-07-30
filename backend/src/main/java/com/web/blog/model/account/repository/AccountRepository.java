package com.web.blog.model.account.repository;

import com.web.blog.model.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Integer>{
  Account findByUseremail(String useremail);
}
