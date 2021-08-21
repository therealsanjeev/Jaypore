package com.groupsale.Ecomm;

public class ModelClass {

    private final int imageview;
    private final String textview1;
    private final String textview2;
    private final String textview3;
    //new code
    private String divider;


    ModelClass(int imageview, String textview1, String textview2, String textview3) {
        this.imageview = imageview;
        this.textview1 = textview1;
        this.textview2 = textview2;
        this.textview3 = textview3;
    }

    public int getImageview() {
        return imageview;
    }

    public String getTextview1() {
        return textview1;
    }

    public String getTextview2() {
        return textview2;
    }

    public String getTextview3() {
        return textview3;  //clock
    }


}
