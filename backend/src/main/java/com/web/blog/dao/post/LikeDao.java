package com.web.blog.dao.post;

import com.web.blog.model.post.Like;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeDao extends JpaRepository<Like, Integer> {
    Like findByPidAndUid(int uid, int pid);
}
