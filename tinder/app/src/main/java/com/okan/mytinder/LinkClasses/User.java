package com.okan.mytinder.LinkClasses;

import android.widget.TextView;

public class User {
    private String id;
    private String userpicture;
    private String username;
    private String city;

    public User(String id, String userpicture, String username, String city) {
        this.id = id;
        this.userpicture = userpicture;
        this.username = username;
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public User() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserpicture() {
        return userpicture;
    }

    public void setUserpicture(String userpicture) {
        this.userpicture = userpicture;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
