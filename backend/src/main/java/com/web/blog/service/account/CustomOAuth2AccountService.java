package com.web.blog.service.account;


import com.web.blog.enums.AccountGrade;
import com.web.blog.model.account.Account;
import com.web.blog.model.account.OAuthAttributes;
import com.web.blog.model.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.*;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Collections;


@RequiredArgsConstructor
@Service
public class CustomOAuth2AccountService extends DefaultOAuth2UserService {
  private final AccountRepository accountRepository;
  private final HttpSession httpSession;

  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    OAuth2UserService delegate = new DefaultOAuth2UserService();
    OAuth2User oAuth2User = delegate.loadUser(userRequest);

    String registrationId = userRequest.getClientRegistration().getRegistrationId();
    String userNameAttributeName = userRequest.getClientRegistration()
            .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

    OAuthAttributes attributes = OAuthAttributes.
            of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

    Account account = saveOrUpdate(attributes);

    return new DefaultOAuth2User(
            Collections.singleton(new SimpleGrantedAuthority(account.getGrade().getValue())),
            attributes.getAttributes(),
            attributes.getNameAttributeKey());
  }
  private Account saveOrUpdate(OAuthAttributes attributes) {
    Account account = accountRepository.findByUseremail(attributes.getEmail());
    if(account == null){
      account = attributes.toEntity();
      account.setPassword(" ");
      account.setGrade(AccountGrade.USER);
    } else{
      account.setUsername(attributes.getName());
    }
    return accountRepository.save(account);
  }
}
