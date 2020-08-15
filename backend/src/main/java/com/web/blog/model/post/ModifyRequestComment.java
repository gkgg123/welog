package com.web.blog.model.post;


import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ModifyRequestComment {
  @Id  @GeneratedValue
  private Integer cid;

  @NonNull
  private Integer pid;

  @NonNull
  private String rstring;

  @NonNull
  private String rcomment;

  @NonNull
  private String cwriter;

  @NonNull
  private String pwriter;

  private boolean ischecked;

  private boolean willModify;

  private boolean isModified;

}
