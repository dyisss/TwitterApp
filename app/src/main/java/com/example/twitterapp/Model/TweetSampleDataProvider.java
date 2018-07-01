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

    public static ArrayList<Tweet>tweetsSearched;
    public static ArrayList<Tweet>tweetsTimeline;
    public static ArrayList<Tweet>tweetsDetailed;
    public static ArrayList<Tweet>profileTimeline;
    public static ArrayList<Tweet>mentionTimeline;
    public static User currentUser;
    public static ArrayList<User> usersSearched;
    public static ArrayList<User> userFollowers;

    static{
        tweetsSearched = new ArrayList<>();
        tweetsTimeline = new ArrayList<>();
        tweetsDetailed = new ArrayList<>();
        usersSearched = new ArrayList<>();
        profileTimeline = new ArrayList<>();
        mentionTimeline = new ArrayList<>();
        userFollowers = new ArrayList<>();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void parseJSONData(String jsonString, List<Tweet> tweetList){
        try{
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray statusesArray = jsonObject.getJSONArray("statuses");
           if(tweetList.size()<statusesArray.length()){
                for(int i = 0 ; i <statusesArray.length() ; i++){
                    JSONObject tweetObject = statusesArray.getJSONObject(i);
                    Tweet tweet = new Tweet(tweetObject);
                    tweetList.add(tweet);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void setCurrentUser(String jsonString){
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            if(currentUser==null) {
                currentUser = new User((JSONObject) jsonObject.get("user"));
                currentUser.setProfile_image_url(currentUser.getProfile_image_url());
                currentUser.setId_str(currentUser.getId_str());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void parseProfileTimelineData(String jsonString, List<Tweet> tweetList){
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray statusesArray = jsonObject.getJSONArray("statuses");
            if(tweetList.size()<statusesArray.length()) {
                for (int i = 0; i < statusesArray.length(); i++) {
                    JSONObject tweetObject = statusesArray.getJSONObject(i);
                    Tweet tweet = new Tweet(tweetObject);
                    tweetList.add(tweet);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void parseMentionTimelineData(String jsonString, List<Tweet> tweetList) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray statusesArray = jsonObject.getJSONArray("statuses");
            if (tweetList.size() < statusesArray.length()) {
                for (int i = 0; i < statusesArray.length(); i++) {
                    JSONObject tweetObject = statusesArray.getJSONObject(i);
                    Tweet tweet = new Tweet(tweetObject);
                    tweetList.add(tweet);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public static void parseUserFriendsData(String jsonString, List<User> userList) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray usersArray = jsonObject.getJSONArray("users");
            if (userList.size() < usersArray.length()) {
                for (int i = 0; i < usersArray.length(); i++) {
                    JSONObject userObject = usersArray.getJSONObject(i);
                    User user = new User(userObject);
                    userList.add(user);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
