package com.groupsale.Ecomm.models;

import java.util.ArrayList;
import java.util.List;

public class DealModel {


    public DealModel(String dealID, String productID, String textMessage, String description, String name, String creatorID, String dateTime, String peopleLeft, String pinCode, String teamSize, String dealPrice, String creatorName, String uniqueFilter, String audioFileLink, String subscriberID, String originalPrice, long epochTime, int status, int unique, int likeCounter, int likeLeft, List<String> currentSubscribers, List<String> currentLikers, List<imageUrl> imageUrl) {
        this.dealID = dealID;
        this.productID = productID;
        this.textMessage = textMessage;
        this.description = description;
        this.name = name;
        this.creatorID = creatorID;
        this.dateTime = dateTime;
        this.peopleLeft = peopleLeft;
        this.pinCode = pinCode;
        this.teamSize = teamSize;
        this.dealPrice = dealPrice;
        this.creatorName = creatorName;
        this.uniqueFilter = uniqueFilter;
        this.audioFileLink = audioFileLink;
        this.subscriberID = subscriberID;
        this.originalPrice = originalPrice;
        this.epochTime = epochTime;
        this.status = status;
        this.unique = unique;
        this.likeCounter = likeCounter;
        this.likeLeft = likeLeft;
        this.currentSubscribers = currentSubscribers;
        this.currentLikers = currentLikers;
        ImageUrl = imageUrl;
    }

    String dealID,productID,textMessage,description,name,creatorID,dateTime, peopleLeft,pinCode,teamSize,dealPrice, creatorName,uniqueFilter,audioFileLink,subscriberID,originalPrice;
    long epochTime;
    int status,unique,likeCounter,likeLeft;
    List<String> currentSubscribers = new ArrayList<String>();
    List<String> currentLikers = new ArrayList<String>();
    List<imageUrl> ImageUrl = new ArrayList<imageUrl>() ;

    public List<String> getCurrentLikers() {
        return currentLikers;
    }

    public void setCurrentLikers(List<String> currentLikers) {
        this.currentLikers = currentLikers;
    }

    public int getLikeLeft() {
        return likeLeft;
    }

    public void setLikeLeft(int likeLeft) {
        this.likeLeft = likeLeft;
    }

    public DealModel() {
    }


    public String getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(String originalPrice) {
        this.originalPrice = originalPrice;
    }

    public String getSubscriberID() {
        return subscriberID;
    }

    public void setSubscriberID(String subscriberID) {
        this.subscriberID = subscriberID;
    }

    public String getAudioFileLink() {
        return audioFileLink;
    }

    public void setAudioFileLink(String audioFileLink) {
        this.audioFileLink = audioFileLink;
    }

    public int getLikeCounter() {
        return likeCounter;
    }

    public void setLikeCounter(int likeCounter) {
        this.likeCounter = likeCounter;
    }

    public String getUniqueFilter() {
        return uniqueFilter;
    }

    public void setUniqueFilter(String uniqueFilter) {
        this.uniqueFilter = uniqueFilter;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getUnique() {
        return unique;
    }

    public void setUnique(int unique) {
        this.unique = unique;
    }

    public long getEpochTime() {
        return epochTime;
    }

    public void setEpochTime(long epochTime) {
        this.epochTime = epochTime;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getDealID() {
        return dealID;
    }

    public void setDealID(String dealID) {
        this.dealID = dealID;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getTextMessage() {
        return textMessage;
    }

    public void setTextMessage(String textMessage) {
        this.textMessage = textMessage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreatorID() {
        return creatorID;
    }

    public void setCreatorID(String creatorID) {
        this.creatorID = creatorID;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getPeopleLeft() {
        return peopleLeft;
    }

    public void setPeopleLeft(String peopleLeft) {
        this.peopleLeft = peopleLeft;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public String getTeamSize() {
        return teamSize;
    }

    public void setTeamSize(String teamSize) {
        this.teamSize = teamSize;
    }

    public String getDealPrice() {
        return dealPrice;
    }

    public void setDealPrice(String dealPrice) {
        this.dealPrice = dealPrice;
    }

    public List<String> getCurrentSubscribers() {
        return currentSubscribers;
    }

    public void setCurrentSubscribers(List<String> currentSubscribers) {
        this.currentSubscribers = currentSubscribers;
    }

    public List<imageUrl> getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(List<imageUrl> imageUrl) {
        ImageUrl = imageUrl;
    }
}
