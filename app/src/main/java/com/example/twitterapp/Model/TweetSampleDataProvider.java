package com.example.twitterapp.Model;

import android.os.Build;
import android.support.annotation.RequiresApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kyle on 22-Jun-18.
 */

public class TweetSampleDataProvider {

    public static ArrayList<Tweet>tweetsTimeline;
    public static  ArrayList<Tweet> detailedTweets;
    public static User currentUser;
    public static ArrayList<User> usersSearched;

    static{
        tweetsTimeline = new ArrayList<>();
        detailedTweets = new ArrayList<>();
        usersSearched = new ArrayList<>();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void parseJSONData(String jsonString, List<Tweet> tweetList){
        try{
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObject.getJSONArray("statuses");
            if(tweetList.size() != jsonArray.length()){
                for(int i = 0 ; i <jsonArray.length() ; i++){
                    JSONObject tweetObject = jsonArray.getJSONObject(i);
                    Tweet tweet = new Tweet(tweetObject);
                    tweetList.add(tweet);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
