package com.example.user.outstagram.MyPost;

import java.util.HashMap;
import java.util.Map;

public class MyPostItem {
    String image;
    String title;
    String formatDate;

    public int starCount = 0;
    public Map<String, Boolean> stars = new HashMap<>();

    public MyPostItem(){

    }

    public int getStarCount() {
        return starCount;
    }

    public void setStarCount(int starCount) {
        this.starCount = starCount;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public String getFormatDate() {
        return formatDate;
    }

    public void setFormatDate(String formatDate) {
        this.formatDate = formatDate;
    }

}
