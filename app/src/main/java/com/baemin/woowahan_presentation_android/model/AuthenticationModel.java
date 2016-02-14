package com.baemin.woowahan_presentation_android.model;

/**
 * Created by leetaejun on 2016. 2. 14..
 */
public class AuthenticationModel {
    private String access_token;
    private UserModel user;

    public String getAccess_token() {
        return access_token;
    }

    public UserModel getUser() {
        return user;
    }
}
