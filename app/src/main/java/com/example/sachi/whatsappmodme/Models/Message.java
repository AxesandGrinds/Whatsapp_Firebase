package com.example.sachi.whatsappmodme.Models;

public class Message {
    private String message,time,sender_user_id;

    public Message(String message, String time, String sender_user_id) {
        this.message = message;
        this.time = time;
        this.sender_user_id = sender_user_id;
    }

    public Message(){

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSender_user_id() {
        return sender_user_id;
    }

    public void setSender_user_id(String sender_user_id) {
        this.sender_user_id = sender_user_id;
    }
}
