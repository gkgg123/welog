package com.web.blog.model.post.repository;

import com.web.blog.model.post.Mrcomment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ModifyRequestCommentRepository extends JpaRepository<Mrcomment, Integer> {
  Mrcomment findByCid(int cid);
  Mrcomment findByPid(int pid);
  List<Mrcomment> findAllByPwriter(String pwriter);
  List<Mrcomment> findAllByCwriter(String cwriter);
  List<Mrcomment> findAllByPid(int pid);
}
