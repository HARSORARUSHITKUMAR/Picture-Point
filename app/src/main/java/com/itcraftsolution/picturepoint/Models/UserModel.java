package com.itcraftsolution.picturepoint.Models;

public class UserModel {
    private String username;
    private ProfileModel profile_image;

    public UserModel(String username, ProfileModel profile_image) {
        this.username = username;
        this.profile_image = profile_image;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ProfileModel getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(ProfileModel profile_image) {
        this.profile_image = profile_image;
    }
}
