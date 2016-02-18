package com.baemin.woowahan_presentation_android.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by leetaejun on 2016. 2. 16..
 */
public class CommentsModel implements Serializable {
    private List<CommentModel> rows;

    public List<CommentModel> getRows() {
        return rows;
    }
}
