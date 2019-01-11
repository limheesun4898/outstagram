package com.example.user.outstagram.Fragment;

import java.util.HashMap;
import java.util.Map;

public class HomeItem {

    public Map<String, Boolean> followering = new HashMap<>();

    public Map<String, Boolean> getFollowering() {
        return followering;
    }

    public void setFollowering(Map<String, Boolean> followering) {
        this.followering = followering;
    }

    public HomeItem() {
    }

}
