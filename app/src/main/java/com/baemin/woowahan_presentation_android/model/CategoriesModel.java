package com.baemin.woowahan_presentation_android.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by leetaejun on 2016. 2. 18..
 */
public class CategoriesModel implements Serializable {
    private List<CategoryModel> rows;

    public List<CategoryModel> getRows() {
        return rows;
    }
}
