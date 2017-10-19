package com.kakitu.kakitu.model;

/**
 * Created by android-dev on 10/16/17.
 */

public class Account {
    private String name;
    private int thumbnail;

    public Account(String name, int thumbnail) {
        this.name = name;
        this.thumbnail = thumbnail;
    }

    public Account(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }
}
