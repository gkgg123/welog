package com.web.blog.model.post;

import com.web.blog.model.account.Account;

public class UserAndPost {
    private PostInfo postInfo;
    private Account account;

    public PostInfo getPostInfo() {
        return postInfo;
    }

    public void setPostInfo(PostInfo postInfo) {
        this.postInfo = postInfo;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public UserAndPost(PostInfo postInfo, Account account){
        this.postInfo = postInfo;
        this.account = account;
    }
}
