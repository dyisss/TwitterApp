package com.example.twitterapp.Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Kyle on 08-Jun-18.
 */

public class Description {
    private ArrayList<Url>urlList = new ArrayList<>();



    public Description(){

    }

    public Description(JSONObject object) {
        try{
            JSONArray descriptionArray = object.getJSONArray("description");
            for (int i = 0 ; i<descriptionArray.length();i++){
                urlList.add((Url) descriptionArray.get(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
