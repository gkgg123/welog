package com.web.blog.model.post.repository;

import com.web.blog.model.post.ModifyRequestComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ModifyRequestCommentRepository extends JpaRepository<ModifyRequestComment, Integer> {
  ModifyRequestComment findByCid(int cid);
  ModifyRequestComment findByPid(int pid);
  List<ModifyRequestComment> findAllByPwriter(String pauthor);
  List<ModifyRequestComment> findAllByPid(int pid);
}
