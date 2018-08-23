package com.example.twitterapp.Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Kyle on 08-Jun-18.
 */

class Entity {
    private ArrayList<Hashtag> Hashtaglist = new ArrayList<>();
    private ArrayList<User_mention> user_mentionslist = new ArrayList<>();
    private ArrayList<Url> urlslist = new ArrayList<>();

   public Entity() {

    }

    public Entity(JSONObject object) {
        try {
            JSONArray entityArray = object.getJSONArray("entities");
            for (int i = 0; i < entityArray.length(); i++) {
                if (entityArray.get(i) instanceof Hashtag) {
                    Hashtaglist.add((Hashtag) entityArray.get(i));
                }else if(entityArray.get(i) instanceof User_mention){
                    user_mentionslist.add((User_mention) entityArray.get(i));
                }else{
                    urlslist.add((Url) entityArray.get(i));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
