package com.group15.TUKulinarium.payload.response;

import com.group15.TUKulinarium.models.Comment;

public class SimpleCommentResponse {
    private String content;

    private SimpleUserResponse user;

    public SimpleCommentResponse(Comment comment) {
        this.content = comment.getContent();
        this.user = new SimpleUserResponse(comment.getUser());
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public SimpleUserResponse getUser() {
        return user;
    }

    public void setUser(SimpleUserResponse user) {
        this.user = user;
    }
}
