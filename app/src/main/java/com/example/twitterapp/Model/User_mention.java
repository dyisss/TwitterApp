package com.example.twitterapp.Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by yang- on 08/06/2018.
 */

public class User_mention {
    private String screen_name;
    private String name;
    private int id;
    private String id_str;
    private int[] indices;

    public User_mention(String screen_name , String name , int id , String id_str , int [] indices){
        this.screen_name = screen_name;
        this.name = name;
        this.id = id;
        this.id_str = id_str;
        this.indices = indices;
    }

    public User_mention(JSONObject object){
        try {
            this.screen_name = object.getString("screen_name");
            this.name = object.getString("name");
            this.id = object.getInt("id");
            this.id_str = object.getString("id_str");
            JSONArray indicesArray = object.getJSONArray("indices");
            for(int i = 0; i<indicesArray.length();i++){
                this.indices[i]=indicesArray.getInt(i);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getScreen_name() {
        return screen_name;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getId_str() {
        return id_str;
    }

    public int[] getIndices() {
        return indices;
    }
}
