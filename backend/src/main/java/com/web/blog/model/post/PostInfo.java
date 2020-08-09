package com.web.blog.model.post;

public class PostInfo {
    private Post post;
    private int likeCount;

    public PostInfo(Post post, int likeCount){
        this.post = post;
        this.likeCount = likeCount;
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
}
