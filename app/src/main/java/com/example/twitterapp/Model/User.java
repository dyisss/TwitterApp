package com.example.twitterapp.Model;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by yang- on 08/06/2018.
 */

public class User {
    private final String TAG = "User";
    private int id;
    private String id_str;
    private String name;
    private String screen_name;
    private String location;
    private String description;
    private int followers_count;
    private int friends_count;
    protected int favourites_count;
    private int utc_offset;
    private String profile_image_url;
    private String profile_banner_url;


    public User(JSONObject object) throws JSONException {
        this.id = object.getInt("id");
        this.id_str = object.getString("id_str");
        this.name = object.getString("name");
        this.screen_name = object.getString("screen_name");
        this.location = object.getString("location");
        this.description = object.getString("description");
        this.followers_count = object.getInt("followers_count");
        this.friends_count = object.getInt("friends_count");
        this.favourites_count = object.getInt("favourites_count");
        this.profile_image_url = object.getString("profile_image_url").replace("_normal", "");
        this.profile_banner_url = object.getString("profile_banner_url");
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getId_str() {
        return id_str;
    }

    public void setId_str(String id_str) {
        this.id_str = id_str;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScreen_name() {
        return screen_name;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    public int getFollowers_count() {
        return followers_count;
    }


    public int getFriends_count() {
        return friends_count;
    }

    public String getProfile_image_url() {
        return profile_image_url;
    }

    public void setProfile_image_url(String profile_image_url) {
        this.profile_image_url = profile_image_url;
    }

    public String getProfile_banner_url() {
        return profile_banner_url;
    }
}