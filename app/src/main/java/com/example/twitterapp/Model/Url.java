package com.example.twitterapp.Model;

/**
 * Created by Kyle on 08-Jun-18.
 */

public class Url extends Entity {
    private String url;
    private String expanded_url ;
    private String display_url;
    private int[] indices;

    public Url(String url , String expanded_url , String display_url , int[] indices){
        this.url = url;
        this.expanded_url = expanded_url;
        this.display_url = display_url;
        this.indices = indices;

    }

    public String getUrl() {
        return url;
    }

    public String getExpanded_url() {
        return expanded_url;
    }

    public String getDisplay_url() {
        return display_url;
    }

    public int[] getIndices() {
        return indices;
    }
}
