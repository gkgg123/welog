package com.web.blog.dao.post;

import java.util.List;
import java.util.Optional;

import com.web.blog.model.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostDao extends JpaRepository<Post, String> {
    List<Post> getPostByAuthor(String author);
//    List<Post> findAllOrderByCount();
//    List<Post> findAllByCreateDate();
    Post getPostByAuthorAndPid(String author, int pid);

//    List<Post> getPostByTagLike(String tag);
//    List<Post> findPostByTagLike(String tag);
    List<Post> findByTagsContaining(String tags);
    //List<Post> getAllByTagLike(String tag);

    //Optional<Post> findPostByEmailAndPassword(String email, String password);

}
