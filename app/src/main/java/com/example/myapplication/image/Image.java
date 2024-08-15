package com.example.myapplication.image;

import android.graphics.Bitmap;

import com.example.myapplication.image.ImageHelper;

import java.io.Serializable;

// Image class with all required constructors for the application.
// Depending on context, there is no need for IDs.
public class Image {

    private int _id;
    private String title;
    private String date;
    private Bitmap data;

    public Image(String title, String date) {
        this.title = title;
        this.date = date;
    }

    public Image(int _id, String title, String date) {
        this(title, date);
        this._id = _id;
    }

    public Image(String title, String date, Bitmap data) {
        this.data = data;
        this.title = title;
        this.date = date;
    }

    public Image(int _id, String name, String date, Bitmap data) {
        this(_id, name, date);
        this.data = data;
    }

    public int getID() {
        return _id;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public Bitmap getData() {
        return data;
    }

    public boolean validate() {
        if(data == null)
            return false;
        return true;
    }

}