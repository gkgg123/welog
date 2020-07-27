package com.web.blog.model.account;

import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Account {
  @Id @GeneratedValue
  private Integer id;

  @Column(unique = true)
  private String useremail;

  private String password;

  private String grade;

  private String username;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getUseremail() {
    return useremail;
  }

  public void setUseremail(String useremail) {
    this.useremail = useremail;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getGrade() {
    return grade;
  }

  public void setGrade(String grade) {
    this.grade = grade;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }
  public void encodePassword(PasswordEncoder passwordEncoder){
    this.password = passwordEncoder.encode(this.password);
  }

}
