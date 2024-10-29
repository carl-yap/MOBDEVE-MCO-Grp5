package com.mobdeve.s21.yap.carl.mco2;

import java.util.ArrayList;
import java.util.List;

public class Album {
    private String name;
    private List<Image> images;

    public Album(String name, List<Image> images) {
        this.name = name;
        this.images = images;
    }

    public Album(String name) {
        this.name = name;
        this.images = new ArrayList<>();
    }

    public int getSize() {
        return images.size();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addImage(Image i) {
        this.images.add(i);
    }

    public void removeImage(int position) {
        this.images.remove(position);
    }

    public String getCoverImage() {
        return images.get(0).getImageRes();
    }
}
