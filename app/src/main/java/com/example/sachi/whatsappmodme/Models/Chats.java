package com.example.sachi.whatsappmodme.Models;

public class Chats {
     private String username,timestamp,profile_pic_url,message;

    public Chats(String username, String timestamp, String profile_pic_url, String message) {
        this.username = username;
        this.timestamp = timestamp;
        this.profile_pic_url = profile_pic_url;
        this.message = message;
    }
    public Chats(){

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getProfile_pic_url() {
        return profile_pic_url;
    }

    public void setProfile_pic_url(String profile_pic_url) {
        this.profile_pic_url = profile_pic_url;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
