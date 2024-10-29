package com.mobdeve.s21.yap.carl.mco2;


import java.io.Serializable;

public class Image implements Serializable {
    private String imageRes;
    private String title;

    public Image(String title, String filePath) {
        this.title = title;
        this.imageRes = filePath;
    }

    public String getImageRes() {
        return imageRes;
    }

    public void setImageRes(String imageRes) {
        this.imageRes = imageRes;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
