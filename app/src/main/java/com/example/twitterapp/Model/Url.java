package com.example.twitterapp.Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    public Url(JSONObject object){
        try {
            this.url = object.getString("url");
            this.expanded_url = object.getString("expaned_url");
            this.display_url = object.getString("display_url");
            JSONArray indicesArray = object.getJSONArray("indices");
            for(int i = 0; i<indicesArray.length();i++){
                this.indices[i]=indicesArray.getInt(i);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

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
