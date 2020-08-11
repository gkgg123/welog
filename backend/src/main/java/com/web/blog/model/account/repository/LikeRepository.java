package com.web.blog.model.account.repository;

import com.web.blog.model.post.LikeInfo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LikeRepository extends JpaRepository<LikeInfo, Integer> {
    LikeInfo findByPidAndUsername(int pid, String username);
    int countByPid(int pid);
    @Query("SELECT r.username FROM LikeInfo r where r.pid = :pid")
    List<String> findByPid(int pid);

}
