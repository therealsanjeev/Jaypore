package com.groupsale.Ecomm.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class products {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("sku")
    @Expose
    private String sku;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("price")
    @Expose
    private String price;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("custom_attributes")
    @Expose
    private List<attributes> attributes = null;

    @SerializedName("media_gallery_entries")
    @Expose
    private List<image> images = null;

    public List<image> getImages() {
        return images;
    }

    public void setImages(List<image> images) {
        this.images = images;
    }

    public List<com.groupsale.Ecomm.models.attributes> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<com.groupsale.Ecomm.models.attributes> attributes) {
        this.attributes = attributes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
