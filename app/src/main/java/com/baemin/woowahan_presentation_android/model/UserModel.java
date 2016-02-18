package com.baemin.woowahan_presentation_android.model;

import java.io.Serializable;

/**
 * Created by leetaejun on 2016. 2. 12..
 */
public class UserModel implements Serializable {

    private int id;
    private String email;
    private String password;
    private String fullname;
    private int team_id;
    private String team_name;
    private ImageModel image;
    private int presentations_count;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public int getTeam_id() {
        return team_id;
    }

    public void setTeam_id(int team_id) {
        this.team_id = team_id;
    }

    public String getTeam_name() {
        return team_name;
    }

    public void setTeam_name(String team_name) {
        this.team_name = team_name;
    }

    public ImageModel getImage() {
        return image;
    }

    public void setImage(ImageModel image) {
        this.image = image;
    }

    public int getPresentations_count() {
        return presentations_count;
    }

    public void setPresentations_count(int presentations_count) {
        this.presentations_count = presentations_count;
    }
}
