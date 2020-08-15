package com.web.blog.service.post;

import com.web.blog.model.BasicResponse;
import com.web.blog.model.post.ModifyRequestComment;
import com.web.blog.model.post.repository.ModifyRequestCommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ModifyRequestService {

  @Autowired
  ModifyRequestCommentRepository repository;

  public ModifyRequestComment createNew(ModifyRequestComment comment){
    return this.repository.save(comment);
  }
}
