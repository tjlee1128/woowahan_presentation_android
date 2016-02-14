package com.baemin.woowahan_presentation_android.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by leetaejun on 2016. 2. 12..
 */
public class PresentationsModel implements Serializable {
    private List<PresentationModel> rows;

    public List<PresentationModel> getRows() {
        return rows;
    }
}
