package com.example.mucahit.mylistapp.Data;

import android.graphics.Bitmap;

public class noteList {

    // variables noteList

    private int id;
    private String title;
    private String time;
    private String date;
    private String title_content;
    private byte[] image;
    private int notification;


    // Constructors noteList

    public noteList(){

    }

    public noteList(String title, String time){
        this.title = title;
        this.time = time;
    }

    public noteList(int id, String title, String content, String time, String date, byte[] image, int notification){

        this.id = id;
        this.title = title;
        this.title_content = content;
        this.time = time;
        this.date = date;
        this.image = image;
        this.notification = notification;

    }

    public noteList(String title, String content, String time, String date, byte[] image){

        this.title = title;
        this.title_content = content;
        this.time = time;
        this.date = date;
        this.image = image;

    }

    public noteList(int id, String title, String title_content, String time, String date, int notification){

        this.id = id;
        this.title = title;
        this.title_content = title_content;
        this.time = time;
        this.date = date;
        this.notification = notification;

    }

    // set noteList

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public void setNotification(int notification) { this.notification = notification;}

    public void setContent(String title_content) { this.title_content = title_content; }

    // get noteList

    public int getId() { return id; }

    public String getTitle() { return title; }

    public String getTime() { return time; }

    public String getDate() { return date; }

    public byte[] getImage() { return image; }

    public int getNotification() { return notification; }

    public String getContent() { return title_content; }
}
