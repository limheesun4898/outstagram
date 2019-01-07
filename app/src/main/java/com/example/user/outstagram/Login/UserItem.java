package com.example.user.outstagram.Login;

import java.util.HashMap;
import java.util.Map;

public class UserItem {

    String name;
    String email;
    String nickname;
    String photo;

    public int follower_count = 0;
    public Map<String, Boolean> follower = new HashMap<>();

    public int following_count = 0;
    public Map<String, Boolean> followering = new HashMap<>();

    public UserItem() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

}
