package com.web.blog.model.account;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Integer>{
  Account findByUseremail(String useremail);
}
