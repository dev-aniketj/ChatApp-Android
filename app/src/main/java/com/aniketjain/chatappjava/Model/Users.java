package com.aniketjain.chatappjava.Model;

public class Users {
    String uid;
    String name;
    String email;
    String image_uri;

    public Users() {
    }

    public Users(String uid, String name, String email, String image_uri) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.image_uri = image_uri;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage_uri() {
        return image_uri;
    }

    public void setImage_uri(String image_uri) {
        this.image_uri = image_uri;
    }
}
