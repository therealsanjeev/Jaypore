package com.groupsale.Ecomm.models;

public class productInfo {

    int subscriberCounter,likeCounter;

    public productInfo() {
    }

    public productInfo(int subscriberCounter, int likeCounter) {
        this.subscriberCounter = subscriberCounter;
        this.likeCounter = likeCounter;
    }

    public int getSubscriberCounter() {
        return subscriberCounter;
    }

    public void setSubscriberCounter(int subscriberCounter) {
        this.subscriberCounter = subscriberCounter;
    }

    public int getLikeCounter() {
        return likeCounter;
    }

    public void setLikeCounter(int likeCounter) {
        this.likeCounter = likeCounter;
    }
}
