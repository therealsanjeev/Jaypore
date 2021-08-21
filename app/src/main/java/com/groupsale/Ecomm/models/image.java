package com.groupsale.Ecomm.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class image {

    @SerializedName("file")
    @Expose
    String file;

    public image() {
    }

    public image(String file) {
        this.file = file;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }
}
