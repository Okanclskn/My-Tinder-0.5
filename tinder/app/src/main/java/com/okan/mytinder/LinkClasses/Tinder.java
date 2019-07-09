package com.okan.mytinder.LinkClasses;

public class Tinder  {
    private String id;
    private String userpicture;

    public Tinder(String id, String userpicture) {
        this.id = id;
        this.userpicture = userpicture;
    }

    public Tinder() {
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
}
