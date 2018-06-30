package com.example.twitterapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Kyle on 08-Jun-18.
 */

public class Userprofile extends Activity {
    private ImageView pUserImage;
    private ImageView pBanner;
    private TextView  pUsername;
    private TextView  pUserScreenName;
    private TextView  pLocation;
    private TextView  pBirthday;
    private TextView  followingCount;
    private TextView  followerCount;
    private TextView  pUserBio;
    private TextView  pLikes;
    private TextView  pReplies;
    private TextView  pTweetsLabel;
    private TextView  pMedia;
    private TextView  pTweetList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        pUserImage = findViewById(R.id.pUserimage);
        pBanner = findViewById(R.id.pUserBanner);
        pUsername = findViewById(R.id.pUsername);
        pUserScreenName = findViewById(R.id.pUserScreenName);
        pLocation = findViewById(R.id.pLocation);
        pBirthday = findViewById(R.id.pBirthday);
        followingCount = findViewById(R.id.followingCount);
        followerCount = findViewById(R.id.followerCount);
        pUserBio = findViewById(R.id.pUserBio);
        pLikes = findViewById(R.id.pLikes);
        pReplies = findViewById(R.id.pReplies);
        pTweetsLabel = findViewById(R.id.pTweetsLabel);
        pMedia = findViewById(R.id.pMedia);
        pTweetList = findViewById(R.id.pTweetlist);
    }
}
