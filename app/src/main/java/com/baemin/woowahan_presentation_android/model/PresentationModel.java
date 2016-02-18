package com.baemin.woowahan_presentation_android.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by leetaejun on 2016. 2. 12..
 */
public class PresentationModel implements Serializable {

    private int id;
    private int comment_count;
    private int view_count;
    private int thumbs_count;
    private boolean thumbs;
    private int category_id;
    private String title;
    private String subtitle;
    private String content;
    private String created_at;
    private String updated_at;
    private UserModel user;
    private VideoModel video;
    private PdfModel pdf;
    private List<ImageModel> images;
    private List<CommentModel> comments;

    public int getId() {
        return id;
    }

    public void setComment_count(int comment_count) {
        this.comment_count = comment_count;
    }

    public int getComment_count() {
        return comment_count;
    }

    public int getView_count() {
        return view_count;
    }

    public int getThumbs_count() {
        return thumbs_count;
    }

    public boolean isThumbs() {
        return thumbs;
    }

    public int getCategory_id() {
        return category_id;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getContent() {
        return content;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public UserModel getUser() {
        return user;
    }

    public VideoModel getVideo() {
        return video;
    }

    public PdfModel getPdf() {
        return pdf;
    }

    public List<ImageModel> getImages() {
        return images;
    }

    public List<CommentModel> getComments() {
        return comments;
    }
}
