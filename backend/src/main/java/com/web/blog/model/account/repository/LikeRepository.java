package com.web.blog.model.account.repository;

import com.web.blog.model.post.LikeInfo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikeRepository extends JpaRepository<LikeInfo, Integer> {
    LikeInfo findByPidAndUid(int uid, int pid);
    int countByPid(int pid);

}
