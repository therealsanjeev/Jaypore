package com.groupsale.Ecomm.models;

import java.util.ArrayList;
import java.util.List;

public class customer {
    String  customerID, name,  phoneNumber,createdAt,pinCode;
    List<String> leadDeal = new ArrayList<String>();
    List<String> currentDeal = new ArrayList<String>();
    // left refer schema


    public customer() {
    }

    public customer(String customerID, String name, String phoneNumber, String createdAt, String pinCode, List<String> leadDeal, List<String> currentDeal) {
        this.customerID = customerID;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.createdAt = createdAt;
        this.pinCode = pinCode;

        this.leadDeal = leadDeal;
        this.currentDeal = currentDeal;
    }



    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public List<String> getLeadDeal() {
        return leadDeal;
    }

    public void setLeadDeal(List<String> leadDeal) {
        this.leadDeal = leadDeal;
    }

    public List<String> getCurrentDeal() {
        return currentDeal;
    }

    public void setCurrentDeal(List<String> currentDeal) {
        this.currentDeal = currentDeal;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }


}
