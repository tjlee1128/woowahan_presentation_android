package com.baemin.woowahan_presentation_android.model;

/**
 * Created by leetaejun on 2016. 2. 12..
 */
public class UserModel {

    private int id;
    private String email;
    private String password;
    private String fullname;
    private ImageModel image;

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getFullname() {
        return fullname;
    }

    public ImageModel getImage() {
        return image;
    }
}
