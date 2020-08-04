package com.web.blog.dao.Account;

import com.web.blog.model.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountDao extends JpaRepository<Account, String> {
    Account findByUsername(String username);
}
