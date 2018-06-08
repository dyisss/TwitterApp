package com.example.twitterapp.Model;

/**
 * Created by Kyle on 08-Jun-18.
 */

class Hashtag extends Entity {
    private String text;
    private int[] indices;

    public Hashtag(String text , int[]indices){
        this.text = text;
        this.indices = indices;
    }

    public String getText() {
        return text;
    }

    public int[] getIndices() {
        return indices;
    }
}
