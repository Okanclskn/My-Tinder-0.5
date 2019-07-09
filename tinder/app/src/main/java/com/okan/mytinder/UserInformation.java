package com.okan.mytinder;

public class UserInformation {
    private String userid;
    private String username;
    private String aboutme;
    private String education;
    private String hobbies;
    private String location;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public UserInformation(String userid, String username, String aboutme, String education, String hobbies, String location) {
        this.userid = userid;
        this.username = username;
        this.aboutme = aboutme;
        this.education = education;
        this.hobbies = hobbies;
        this.location = location;
    }

    public UserInformation() {
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAboutme() {
        return aboutme;
    }

    public void setAboutme(String aboutme) {
        this.aboutme = aboutme;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getHobbies() {
        return hobbies;
    }

    public void setHobbies(String hobbies) {
        this.hobbies = hobbies;
    }
}

