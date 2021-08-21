package com.groupsale.Ecomm.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class items {

    @SerializedName("items")
    @Expose
    private List<products> products = null;

    public List<com.groupsale.Ecomm.models.products> getProducts() {
        return products;
    }

    public void setProducts(List<com.groupsale.Ecomm.models.products> products) {
        this.products = products;
    }
}
