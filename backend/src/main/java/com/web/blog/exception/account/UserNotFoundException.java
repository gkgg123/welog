package com.web.blog.exception.account;

public class UserNotFoundException extends RuntimeException{

  public UserNotFoundException(String userEmail){
    super(userEmail + " NotFoundException");
  }

}