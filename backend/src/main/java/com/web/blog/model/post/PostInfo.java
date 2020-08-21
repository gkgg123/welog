package com.web.blog.model.post;

import com.web.blog.model.file.Image;

import java.util.List;

public class PostInfo {
    private Post post;
    private int likeCount;
    private int commentCount;
    private List<String> userlist;
    private List<Image> images;
    public PostInfo(Post post, int likeCount, int commentCount, List<String> userlist, List<Image> images){
        this.post = post;
        this.likeCount = likeCount;
        this.userlist = userlist;
        this.images = images;
        this.commentCount = commentCount;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public List<String> getUserlist() {
        return userlist;
    }

    public void setUserlist(List<String> userlist) {
        this.userlist = userlist;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }
}
