package com.example.twitterapp.Model;

import android.os.Build;
import android.support.annotation.RequiresApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Kyle on 11-Jun-18.
 */

public class Tweet {
    private User user;
    private String id ;
    private String text;
    private LocalDateTime created_at;
    private boolean retweeted;
    private boolean liked;


    @RequiresApi(api = Build.VERSION_CODES.O)
    public Tweet(JSONObject jsonObject ){
        try {
            this.id = jsonObject.getString("id_str");
            this.text = jsonObject.getString("text");
            this.user = new User(jsonObject.getJSONObject("user"));
            String date =  jsonObject.getString("created_at");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss Z yyyy");
            this.created_at = LocalDateTime.parse(date,formatter);
            this.retweeted = false;
            this.liked = false;
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
        return DateTimeFormatter.ofPattern("dd MMM yyyy").format(created_at);
    }

    public boolean isRetweeted() {
        return retweeted;
    }

    public void setRetweeted(boolean retweeted) {
        this.retweeted = retweeted;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }
}
