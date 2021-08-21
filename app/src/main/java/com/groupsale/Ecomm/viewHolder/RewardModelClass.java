package com.groupsale.Ecomm.viewHolder;

public class RewardModelClass {

    String cardName, CardImg;
    Integer cardProgress, cardPercentage;

    RewardModelClass() {

    }

    public RewardModelClass(String cardName, String cardImg, Integer cardProgress, Integer cardPercentage) {
        this.cardName = cardName;
        CardImg = cardImg;
        this.cardProgress = cardProgress;
        this.cardPercentage = cardPercentage;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getCardImg() {
        return CardImg;
    }

    public void setCardImg(String cardImg) {
        CardImg = cardImg;
    }

    public Integer getCardProgress() {
        return cardProgress;
    }

    public void setCardProgress(Integer cardProgress) {
        this.cardProgress = cardProgress;
    }

    public Integer getCardPercentage() {
        return cardPercentage;
    }

    public void setCardPercentage(Integer cardPercentage) {
        this.cardPercentage = cardPercentage;
    }
}



