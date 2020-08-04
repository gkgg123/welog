package com.web.blog.model.account.repository;

import java.util.List;
import java.util.Optional;

import com.web.blog.model.post.Comment;
import com.web.blog.model.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, String> {
    List<Comment> getCommentByPid(int pid);
    //    List<Post> findAllOrderByCount();
//    List<Post> findAllByCreateDate();
    Comment getCommentByCid(int cid);
    //Optional<Post> findPostByEmailAndPassword(String email, String password);

}
