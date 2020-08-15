package com.web.blog.model.account.repository;

import com.web.blog.model.post.Intro;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IntroRepository extends JpaRepository<Intro, String> {
    Intro findByAuthor(String author);
}
