package com.example.twitterapp.Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    public Hashtag(JSONObject object){
        try{
            this.text = object.getString("text");
            JSONArray indicesArray = object.getJSONArray("indices");
            for(int i = 0; i<indicesArray.length();i++){
                this.indices[i]=indicesArray.getInt(i);
            }
        }catch (JSONException e){

        }
    }

    public String getText() {
        return text;
    }

    public int[] getIndices() {
        return indices;
    }
}
