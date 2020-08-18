package com.web.blog.handler.account;

import com.web.blog.model.account.AccountDetails;
import com.web.blog.model.post.repository.ModifyRequestCommentRepository;
import com.web.blog.utils.TokenUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Log4j2
public class CustomLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
  @Autowired
  private TokenUtils tokenUtils;

  @Autowired
  private ModifyRequestCommentRepository repository;


  @Override public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
    AccountDetails user = (AccountDetails) authentication.getPrincipal();
    String token = tokenUtils.generateJwtToken(user.getAccount());

    String username = user.getUsername();

    response.addHeader("Authorization","Bearer"+token);
    response.addHeader("MrAlarmCount",Integer.toString(repository.countByPwriter(username)));
  }

}
