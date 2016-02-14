package com.baemin.woowahan_presentation_android.model;

/**
 * Created by leetaejun on 2016. 2. 12..
 */

import java.io.Serializable;

/**
"pdf": {
    "id": 1,
    "file_name": "foodrain_mid_pt.pdf",
    "file_content_type": "application/pdf",
    "file_size": 451188,
    "file_updated_at": "2016-02-12T08:12:38.000Z"
    "url": "/system/pdfs/pdfs/000/000/001/original/foodrain_mid_pt.pdf?1455264758"
}
*/
public class PdfModel implements Serializable {

    private int id;
    private String file_name;
    private String file_content_type;
    private int file_size;
    private String file_updated_at;
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

    public String getUrl() {
        return url;
    }
}
