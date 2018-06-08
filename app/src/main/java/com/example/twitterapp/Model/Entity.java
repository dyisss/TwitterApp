package com.example.twitterapp.Model;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Kyle on 08-Jun-18.
 */

class Entity {
    private ArrayList<Hashtag>Hashtaglist = new ArrayList<>();
    private ArrayList<User_mention>user_mentionslist= new ArrayList<>();
    private ArrayList<Url>urlslist=new ArrayList<>();
    private String symbols;

    public Entity(){

    }

    public Entity(JSONObject object){
    }
}
