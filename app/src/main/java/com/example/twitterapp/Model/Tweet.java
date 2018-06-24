package com.example.twitterapp.Model;

import android.os.Build;
import android.support.annotation.RequiresApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by Kyle on 11-Jun-18.
 */

public class Tweet {
    private User user;
    private String id ;
    private String text;
    private LocalDateTime created_at;


    @RequiresApi(api = Build.VERSION_CODES.O)
    public Tweet(JSONObject jsonObject ){
        try {
            this.id = jsonObject.getString("id_str");
            this.text = jsonObject.getString("text");
            this.user = new User(jsonObject.getJSONObject("user"));
            String date =  jsonObject.getString("created_at");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss Z yyyy");
            this.created_at = LocalDateTime.parse(date);
            this.created_at.format(formatter);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public User getUser() {
        return user;
    }


    public String getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getCreated_at() {
        return DateTimeFormatter.ofPattern("dd/MM/yyyy").format(created_at);
    }
}
