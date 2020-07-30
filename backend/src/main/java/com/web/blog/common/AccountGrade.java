package com.web.blog.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum AccountGrade {
  USER("ROLE_USER"),
  ADMIN("ROLE_ADMIN");

  private String value;

  public String getValue() {
    return value;
  }
}
