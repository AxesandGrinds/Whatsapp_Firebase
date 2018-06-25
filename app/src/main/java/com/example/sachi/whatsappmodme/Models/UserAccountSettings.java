package com.example.sachi.whatsappmodme.Models;

public class UserAccountSettings {
    private String email,username,password,about,about_date,join_date,profile_photo_url;

    public UserAccountSettings(String email, String username, String password, String about, String about_date, String join_date, String profile_photo_url) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.about = about;
        this.about_date = about_date;
        this.join_date = join_date;
        this.profile_photo_url = profile_photo_url;
    }

    public UserAccountSettings(){

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getAbout_date() {
        return about_date;
    }

    public void setAbout_date(String about_date) {
        this.about_date = about_date;
    }

    public String getJoin_date() {
        return join_date;
    }

    public void setJoin_date(String join_date) {
        this.join_date = join_date;
    }

    public String getProfile_photo_url() {
        return profile_photo_url;
    }

    public void setProfile_photo_url(String profile_photo_url) {
        this.profile_photo_url = profile_photo_url;
    }

}
