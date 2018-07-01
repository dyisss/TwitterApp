package com.example.twitterapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.twitterapp.Adapters.TweetListAdapter;
import com.example.twitterapp.Model.AuthenticationWebView;
import com.example.twitterapp.Model.OpenAuthentication;
import com.example.twitterapp.Model.Tweet;
import com.example.twitterapp.Model.TweetSampleDataProvider;
import com.example.twitterapp.Model.User;
import com.example.twitterapp.View.TwitterButtons;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by yang- on 01/07/2018.
 */

public class DetailedActivity extends AppCompatActivity implements Observer {
    private static final String AUTHORISED = "authorised";
    private static final String ACCESS_TOKEN = "Access Token";
    private static final String ACCESS_TOKEN_SECRET = "Access Token Secret";
    private ListView tweetList;
    private static final String PREFS = "Preferences";
    public static ArrayList<Tweet> tweetslist = TweetSampleDataProvider.tweetsTimeline;
    private TweetListAdapter tweetListAdapter;

    //imageView
    private ImageView mSearchBtn;
    private ImageView userImage;
    private ImageView mPost;
    private TwitterButtons twitterButtons;

    //current user
    public static User current;
    public static String userid;


    public final static OpenAuthentication authentication = OpenAuthentication.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailed_tweet);
        authentication.addObserver(this);
        tweetList = findViewById(R.id.tweetsList);
        userImage = findViewById(R.id.acUserimage);
        mSearchBtn = findViewById(R.id.searchBtn);

        int index = getIntent().getIntExtra("mainactivity", -1);

        authentication.getRetweetReactions(tweetslist.get(index).getId());
        TweetListAdapter tweetListSearchedAdapter = new TweetListAdapter(DetailedActivity.this, R.layout.tweet, TweetSampleDataProvider.tweetsDetailed);
        tweetList.setAdapter(tweetListSearchedAdapter);
    }

    public void authorisationIntent() {
        Intent authIntent = new Intent(DetailedActivity.this, AuthenticationWebView.class);
        startActivity(authIntent);
    }

    @Override
    public void update(Observable observable, Object o) {
        tweetListAdapter.notifyDataSetChanged();
        tweetList.invalidate();
    }

    @Override
    protected void onResume() {
        authentication.addObserver(this);
        super.onResume();
        tweetListAdapter.notifyDataSetChanged();
        tweetList.invalidate();
        if (authentication.isAuthorized()) {
            authentication.callTweetTask();
            tweetListAdapter.notifyDataSetChanged();
            tweetList.invalidate();
            authentication.callProfileTimeline();
            authentication.callMentionsTimeline();
            authentication.setUser();
            if (TweetSampleDataProvider.currentUser != null) {
                current = TweetSampleDataProvider.currentUser;
                Picasso.get().load(TweetSampleDataProvider.currentUser.getProfile_image_url()).transform(new TweetListAdapter.CircleTransform()).into(userImage);
                userImage.invalidate();
                tweetListAdapter.notifyDataSetChanged();
                tweetList.invalidate();
                authentication.callMentionsTimeline();
                authentication.friendList(current.getScreen_name());
            }
        } else {
            authorisationIntent();
            fillList(tweetslist);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        tweetListAdapter.notifyDataSetChanged();
        tweetList.invalidate();
    }

    @Override
    protected void onStop() {
        SharedPreferences mSharedPreferences = getSharedPreferences(DetailedActivity.PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();

        if (authentication.isAuthorized()){
            mEditor.putBoolean(AUTHORISED,true);
            mEditor.putString(ACCESS_TOKEN,authentication.getStr_accesstoken());
            mEditor.putString(ACCESS_TOKEN_SECRET,authentication.getStr_access_token_secret());
            mEditor.apply();
        }else {
            mEditor.putBoolean(AUTHORISED,false);
            mEditor.putString(ACCESS_TOKEN,"");
            mEditor.putString(ACCESS_TOKEN_SECRET,"");
            mEditor.apply();
        }

        mEditor.commit();
        super.onStop();
    }


    public void SignOut(View view) {
        SharedPreferences mSharedPreferences = getSharedPreferences(DetailedActivity.PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        if(authentication.isAuthorized()) {
            mEditor.putBoolean(AUTHORISED, false);
            mEditor.putString(ACCESS_TOKEN, "");
            mEditor.putString(ACCESS_TOKEN_SECRET, "");
        }
        mEditor.commit();
        authorisationIntent();
    }

    private void fillList(ArrayList<Tweet> tweets){
        tweetListAdapter = new TweetListAdapter(this, R.layout.tweet, tweets);
        tweetList.setAdapter(tweetListAdapter);
    }
}
