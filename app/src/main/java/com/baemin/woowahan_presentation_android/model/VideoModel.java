package com.baemin.woowahan_presentation_android.model;

/**
 * Created by leetaejun on 2016. 2. 12..
 */

/** JSON Response
"video": {
    "id": 1,
    "file_name": "foodrain_android.mp4",
    "file_content_type": "video/mp4",
    "file_size": 6921424,
    "file_updated_at": "2016-02-12T08:12:20.077Z",
    "thumb_url": "/system/videos/videos/000/000/001/thumb/foodrain_android.jpeg?1455264740",
    "url": "/system/videos/videos/000/000/001/original/foodrain_android.mp4?1455264740"
}
*/
public class VideoModel {

    private int id;
    private String file_name;
    private String file_content_type;
    private int file_size;
    private String file_updated_at;
    private String thumb_url;
    private String url;

    public int getId() {
        return id;
    }

    public String getFile_name() {
        return file_name;
    }

    public String getFile_content_type() {
        return file_content_type;
    }

    public int getFile_size() {
        return file_size;
    }

    public String getFile_updated_at() {
        return file_updated_at;
    }

    public String getThumb_url() {
        return thumb_url;
    }

    public String getUrl() {
        return url;
    }
}
