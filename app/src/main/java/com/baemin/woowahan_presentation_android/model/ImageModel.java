package com.baemin.woowahan_presentation_android.model;

/**
 * Created by leetaejun on 2016. 2. 12..
 */

/** JSON Response
"image": {
    "id": 1,
    "file_name": "user-avatar.jpg",
    "file_content_type": "image/jpeg",
    "file_size": 160093,
    "file_updated_at": "2016-02-12T08:11:29.186Z",
    "medium_url": "/system/images/images/000/000/001/medium/user-avatar.jpg?1455264689",
    "thumb_url": "/system/images/images/000/000/001/thumb/user-avatar.jpg?1455264689",
    "original_url": "/system/images/images/000/000/001/original/user-avatar.jpg?1455264689"
}
*/

public class ImageModel {

    private int id;
    private String file_name;
    private String file_content_type;
    private int file_size;
    private String file_updated_at;
    private String medium_url;
    private String thumb_url;
    private String original_url;

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

    public String getMedium_url() {
        return medium_url;
    }

    public String getThumb_url() {
        return thumb_url;
    }

    public String getOriginal_url() {
        return original_url;
    }
}
