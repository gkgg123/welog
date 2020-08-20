package com.web.blog.model.account;

import com.web.blog.enums.AccountGrade;
import lombok.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Account implements Serializable {
  @Id @GeneratedValue
  private Integer id;

  @NonNull
  @Column(unique = true)
  private String useremail;

  @NonNull
  private String password;

  @NonNull
  @Enumerated(EnumType.STRING)
  private AccountGrade grade;

  @NonNull
  @Column(unique = true)
  private String username;

  private boolean isEnable;

  @Getter
  @Setter
  private String provider;

  @Getter
  @Setter
  private String realname;

  @Getter
  @Setter
  private String profileUrl;

  @Getter
  @Setter
  private String userDescription;


  public boolean isEnable() {
    return isEnable;
  }

  public void setEnable(boolean enable) {
    isEnable = enable;
  }

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

  public AccountGrade getGrade() {
    return grade;
  }
  public void setGrade(AccountGrade grade) {
    this.grade = grade;
  }

  public String getUsername() {
    return username;
  }
  public void setProfileUrl(String profileUrl){ this.profileUrl = profileUrl;}
  public void setUsername(String username) {
    this.username = username;
  }
  public void encodePassword(BCryptPasswordEncoder passwordEncoder){
    this.password = passwordEncoder.encode(this.password);
  }

}
