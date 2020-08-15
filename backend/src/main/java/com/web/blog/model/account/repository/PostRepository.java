package com.web.blog.model.account.repository;

import java.util.List;
import java.util.Optional;

import com.web.blog.model.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PostRepository extends JpaRepository<Post, String> {
    List<Post> getPostByAuthor(String author);
    Post getPostByAuthorAndPid(String author, int pid);
    List<Post> findByTagsContaining(String tags);
    List<Post> findByTitleContaining(String title);
    List<Post> findByContentContaining(String content);
    List<Post> findByTitleAndContent(String title, String content);
    List<Post> findByTitleContainingOrContentContaining(String title, String content);
    Post findByPid(int pid);
    @Query("SELECT max(p.postno) from Post p where p.author = :author")
    int findByAuthor(String author);
    //Optional<Post> findPostByEmailAndPassword(String email, String password);

}
