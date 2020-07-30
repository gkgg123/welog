package com.web.blog.model.account;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Delegate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AccountDetails implements UserDetails {

  @Delegate
  private Account account;
  private Collection<? extends GrantedAuthority> authorities;



  public Account getAccount() {
    return account;
  }

  public void setAccount(Account account) {
    this.account = account;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }
  public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
    this.authorities = authorities;
  }

  @Override
  public boolean isAccountNonExpired() {
    return account.isEnable();
  }

  @Override
  public boolean isAccountNonLocked() {
    return account.isEnable();
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return account.isEnable();
  }

  @Override
  public boolean isEnabled() {
    return account.isEnable();
  }
}
