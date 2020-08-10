package com.web.blog.model.account;

import com.web.blog.enums.AccountGrade;
import lombok.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Entity
@NoArgsConstructor
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

  public boolean isEnable() {
    return isEnable;
  }

  public void setEnable(boolean enable) {
    isEnable = enable;
  }

  @Builder
  public Account(String username, String useremail, String password, AccountGrade grade){
    this.username = username;
    this.useremail = useremail;
    this.password = password;
    this.isEnable = true;
    this.grade = grade;
  }

  public Account update(String name){
    this.username = name;

    return this;
  }
  public void encodePassword(BCryptPasswordEncoder passwordEncoder){
    this.password = passwordEncoder.encode(this.password);
  }

}
