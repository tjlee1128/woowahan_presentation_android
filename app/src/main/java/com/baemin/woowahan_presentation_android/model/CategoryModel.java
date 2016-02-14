package com.baemin.woowahan_presentation_android.model;

import java.io.Serializable;

/**
 * Created by leetaejun on 2016. 2. 14..
 */
public class CategoryModel implements Serializable {
    private int id;
    private String name;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
