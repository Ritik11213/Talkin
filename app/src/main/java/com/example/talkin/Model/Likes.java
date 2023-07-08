package com.example.talkin.Model;

public class Likes {

    String id;
    String likeby;
    String postid;

    public Likes(String id, String likeby, String postid) {
        this.id = id;
        this.likeby = likeby;
        this.postid = postid;
    }

    public Likes(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLikeby() {
        return likeby;
    }

    public void setLikeby(String likeby) {
        this.likeby = likeby;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }
}
