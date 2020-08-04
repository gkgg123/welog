package com.web.blog.dao.post;

import com.web.blog.model.post.LikeInfo;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeDao extends JpaRepository<LikeInfo, Integer> {
    LikeInfo findByPidAndUid(int uid, int pid);
}
