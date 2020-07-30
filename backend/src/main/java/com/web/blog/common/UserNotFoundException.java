package com.web.blog.common;

public class UserNotFoundException extends RuntimeException{

  public UserNotFoundException(String userEmail){
    super(userEmail + " NotFoundException");
  }

}