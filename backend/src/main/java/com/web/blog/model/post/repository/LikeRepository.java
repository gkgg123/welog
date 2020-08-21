package com.web.blog.model.post.repository;

import com.web.blog.model.post.LikeInfo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface LikeRepository extends JpaRepository<LikeInfo, Integer> {
    LikeInfo findByPidAndUsername(int pid, String username);
    //int countByPid(int pid);
    List<LikeInfo> findByPostusername(String postusername);
    List<LikeInfo> findByUsername(String username);
    @Query("SELECT r.username FROM LikeInfo r where r.pid = :pid and r.likeit = 1")
    List<String> findByPid(int pid); // 좋아요 누른 사람 찾기

    @Query("SELECT SUM(li.likeit) from LikeInfo li where li.postusername = :postusername")
    Integer getLikeMeCount(String postusername);

    @Query("SELECT SUM(li.likeit) from LikeInfo li where li.username = :username")
    Integer getLikePostCount(String username);

    @Query("SELECT COUNT(li) FROM LikeInfo li where li.postusername =:postusername and li.likeit = 1 group by li.pid")
    Integer getMostLikes(String postusername);
}
