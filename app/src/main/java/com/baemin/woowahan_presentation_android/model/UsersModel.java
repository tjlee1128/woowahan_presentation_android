package com.baemin.woowahan_presentation_android.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by leetaejun on 2016. 2. 18..
 */
public class UsersModel implements Serializable {
    private List<UserModel> rows;

    public List<UserModel> getRows() {
        return rows;
    }
}
