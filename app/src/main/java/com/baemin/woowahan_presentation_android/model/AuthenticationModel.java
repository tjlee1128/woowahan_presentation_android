package com.baemin.woowahan_presentation_android.model;

import java.io.Serializable;

/**
 * Created by leetaejun on 2016. 2. 14..
 */
public class AuthenticationModel implements Serializable {
    private String access_token;
    private UserModel user;

    public String getAccess_token() {
        return access_token;
    }

    public UserModel getUser() {
        return user;
    }
}
