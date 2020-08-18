package com.web.blog.model.post;

public class CommentInfo {
    Comment comment;
    String profileUrl;

    public CommentInfo(Comment comment, String profileUrl) {
        this.comment = comment;
        this.profileUrl = profileUrl;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }
}
