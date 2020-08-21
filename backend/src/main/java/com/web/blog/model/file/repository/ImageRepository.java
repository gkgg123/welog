package com.web.blog.model.file.repository;

import com.web.blog.model.file.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, String> {
    Image findByIid(String iid);
    Image findByPath(String path);
    List<Image> findByPid(int pid);
}
