package com.web.blog.dao.post;

import java.util.List;
import java.util.Optional;

import com.web.blog.model.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostDao extends JpaRepository<Post, String> {
    List<Post> getPostByAuthor(String author);
    Post getPostByAuthorAndPid(String author, int pid);
    List<Post> findByTagsContaining(String tags);

    //Optional<Post> findPostByEmailAndPassword(String email, String password);

}
