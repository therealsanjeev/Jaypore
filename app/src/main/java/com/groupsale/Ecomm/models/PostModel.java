package com.groupsale.Ecomm.models;

public class PostModel {

    String name,post_image_url,profile_image_url,text;
    int likes;

    public PostModel(String name, String post_image_url, String text, int likes, String profile_image_url) {
        this.name = name;
        this.post_image_url = post_image_url;
        this.profile_image_url = profile_image_url;
        this.text = text;
        this.likes = likes;
    }

    public PostModel() {
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPost_image_url() {
        return post_image_url;
    }

    public void setPost_image_url(String post_image_url) {
        this.post_image_url = post_image_url;
    }

    public String getProfile_image_url() {
        return profile_image_url;
    }

    public void setProfile_image_url(String profile_image_url) {
        this.profile_image_url = profile_image_url;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }
}
