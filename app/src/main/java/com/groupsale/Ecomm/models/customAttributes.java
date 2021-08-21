package com.groupsale.Ecomm.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class customAttributes {

    @SerializedName("custom_attributes")
    @Expose
    private List<attributes> attributes = null;

    public List<com.groupsale.Ecomm.models.attributes> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<com.groupsale.Ecomm.models.attributes> attributes) {
        this.attributes = attributes;
    }
}
