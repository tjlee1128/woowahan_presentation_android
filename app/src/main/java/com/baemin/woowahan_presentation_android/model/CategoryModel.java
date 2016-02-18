package com.baemin.woowahan_presentation_android.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by leetaejun on 2016. 2. 14..
 */
public class CategoryModel implements Serializable {
    private int id;
    private String name;
    private List<PresentationModel> presentations;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<PresentationModel> getPresentations() {
        return presentations;
    }
}
