package com.web.blog.service.post;

import com.web.blog.model.post.Mrcomment;
import com.web.blog.model.post.repository.ModifyRequestCommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ModifyRequestService {

  @Autowired
  ModifyRequestCommentRepository repository;

  public Mrcomment createNew(Mrcomment comment){
    return this.repository.save(comment);
  }
}
