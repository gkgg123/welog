package com.web.blog.model.post;


import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Mrcomment {
  @Id  @GeneratedValue(strategy = GenerationType.IDENTITY)
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

  private int willmodify;

  private boolean ismodified;

  @NonNull
  private String posttitle;

}
