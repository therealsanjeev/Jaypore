package com.groupsale.Ecomm.models;

import java.io.Serializable;

/**
 * Created by Sanjeev on 17,June,2021
 * therealsanjeev0@gmail.com
 */
public class RewardCoinsModel implements Serializable {
    private String button,CardImg,cardName,claim1,claim2,claim3,descline1,descline2,descline3,coins;

    public RewardCoinsModel(){}
    public RewardCoinsModel(String button, String cardImg, String cardName, String claim1, String claim2, String claim3, String descline1, String descline2, String descline3, String coins) {
        this.button = button;
        CardImg = cardImg;
        this.cardName = cardName;
        this.claim1 = claim1;
        this.claim2 = claim2;
        this.claim3 = claim3;
        this.descline1 = descline1;
        this.descline2 = descline2;
        this.descline3 = descline3;
        this.coins = coins;
    }

    public String getButton() {
        return button;
    }

    public void setButton(String button) {
        this.button = button;
    }

    public String getCardImg() {
        return CardImg;
    }

    public void setCardImg(String cardImg) {
        CardImg = cardImg;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getClaim1() {
        return claim1;
    }

    public void setClaim1(String claim1) {
        this.claim1 = claim1;
    }

    public String getClaim2() {
        return claim2;
    }

    public void setClaim2(String claim2) {
        this.claim2 = claim2;
    }

    public String getClaim3() {
        return claim3;
    }

    public void setClaim3(String claim3) {
        this.claim3 = claim3;
    }

    public String getDescline1() {
        return descline1;
    }

    public void setDescline1(String descline1) {
        this.descline1 = descline1;
    }

    public String getDescline2() {
        return descline2;
    }

    public void setDescline2(String descline2) {
        this.descline2 = descline2;
    }

    public String getDescline3() {
        return descline3;
    }

    public void setDescline3(String descline3) {
        this.descline3 = descline3;
    }

    public String getCoins() {
        return coins;
    }

    public void setCoins(String coins) {
        this.coins = coins;
    }
}
