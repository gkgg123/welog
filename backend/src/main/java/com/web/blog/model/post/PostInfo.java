package com.web.blog.model.post;

import java.util.List;

public class PostInfo {
    private Post post;
    private int likeCount;
    private List<String> userlist;
    public PostInfo(Post post, int likeCount, List<String> userlist){
        this.post = post;
        this.likeCount = likeCount;
        this.userlist = userlist;
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

    public List<String> getUserlist() {
        return userlist;
    }

    public void setUserlist(List<String> userlist) {
        this.userlist = userlist;
    }
}
