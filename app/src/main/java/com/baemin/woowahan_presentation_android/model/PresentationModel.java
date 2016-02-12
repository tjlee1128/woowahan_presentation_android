package com.baemin.woowahan_presentation_android.model;

import java.util.List;

/**
 * Created by leetaejun on 2016. 2. 12..
 */
public class PresentationModel {

    private int id;
    private String title;
    private String subtitle;
    private String content;
    private String created_at;
    private String updated_at;
    private UserModel user;
    private VideoModel video;
    private PdfModel pdf;
    private List<ImageModel> images;

    public int getId() {
        return id;
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
}
