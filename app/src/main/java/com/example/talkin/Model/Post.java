package com.example.talkin.Model;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class Post
{
    private String id;
    private String sender;
    private String caption;
    private String post;
    private String date;
    List <String> likes;
    List <List<String>> comments;

    public Post(String id, String sender, String caption, String post, String date, List<String> likes, List<List<String>> comments) {
        this.id = id;
        this.sender = sender;
        this.caption = caption;
        this.post = post;
        this.date = date;
        this.likes = likes;
        this.comments = comments;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<String> getLikes() {
        return likes;
    }

    public void setLikes( List<String> likes) {
        this.likes = likes;
    }

    public List<List<String>> getComments() {
        return comments;
    }

    public void setComments(List<List<String>> comments) {
        this.comments = comments;
    }

    public Post () {

    }
}
