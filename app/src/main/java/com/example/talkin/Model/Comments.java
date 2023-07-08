package com.example.talkin.Model;

public class Comments {

    String id;
    String postid;
    String commentby;
    String comment;

    public Comments(){

    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Comments(String id, String postid, String commentby, String comment) {
        this.id = id;
        this.postid = postid;
        this.commentby = commentby;
        this.comment = comment;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public String getCommentby() {
        return commentby;
    }

    public void setCommentby(String commentby) {
        this.commentby = commentby;
    }
}
