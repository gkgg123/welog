package com.web.blog.model.post;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LikeInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int pid;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String username;

    private int likeit;

    public int getLikeit() {
        return likeit;
    }

    public void setLikeit(int likeit) {
        this.likeit = likeit;
    }
}
