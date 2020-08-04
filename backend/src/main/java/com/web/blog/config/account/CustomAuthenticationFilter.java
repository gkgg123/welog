package com.web.blog.config.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.blog.exception.account.InputNotFoundException;
import com.web.blog.model.account.Account;
import lombok.extern.log4j.Log4j2;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.stream.Collectors;

@Log4j2
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
  public CustomAuthenticationFilter(AuthenticationManager authenticationManager){
    super.setAuthenticationManager(authenticationManager);
  }


  public static String getBody(HttpServletRequest request) throws IOException {

    String body = null;
    StringBuilder stringBuilder = new StringBuilder();
    BufferedReader bufferedReader = null;

    try {
      InputStream inputStream = request.getInputStream();
      if (inputStream != null) {
        bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        char[] charBuffer = new char[128];
        int bytesRead = -1;
        while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
          stringBuilder.append(charBuffer, 0, bytesRead);
        }
      }
    } catch (IOException ex) {
      throw ex;
    } finally {
      if (bufferedReader != null) {
        try {
          bufferedReader.close();
        } catch (IOException ex) {
          throw ex;
        }
      }
    }

    body = stringBuilder.toString();
    return body;
  }
  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException{
    UsernamePasswordAuthenticationToken authenticationToken;

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    try {
      Account account = new ObjectMapper().readValue(request.getInputStream(), Account.class);
      authenticationToken = new UsernamePasswordAuthenticationToken(account.getUseremail(), account.getPassword());
    } catch (IOException e){
      throw new InputNotFoundException();
    }
    setDetails(request,authenticationToken);
    return this.getAuthenticationManager().authenticate(authenticationToken);
/*    String abc = "";
    try {
      abc =request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
      System.out.println("I'm here");
      System.out.println(abc);
    } catch (IOException e) {
      e.printStackTrace();
    }

    try {
      System.out.println("And I'm there");
      JSONParser jsonParser = new JSONParser();
      JSONObject jsonObject = (JSONObject) jsonParser.parse(abc);
      String useremail = (String) jsonObject.get("useremail");
      System.out.println(useremail);
      String password = (String) jsonObject.get("password");
      Account account = new Account();
      account.setUseremail(useremail);
      account.setPassword(password);
      authenticationToken = new UsernamePasswordAuthenticationToken(account.getUseremail(),account.getPassword());
    } catch (Exception e) {
      throw new InputNotFoundException();
    }
    setDetails(request,authenticationToken);
    return this.getAuthenticationManager().authenticate(authenticationToken);*/
  }


}
