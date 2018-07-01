package com.example.twitterapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.twitterapp.Adapters.ProfileTweetListAdapter;
import com.example.twitterapp.Adapters.TweetListAdapter;
import com.example.twitterapp.Model.TweetSampleDataProvider;
import com.example.twitterapp.View.TwitterButtons;
import com.squareup.picasso.Picasso;

/**
 * Created by Kyle on 08-Jun-18.
 */

public class Userprofile extends Activity {
    private ImageView pUserImage;
    private ImageView pBanner;
    private TextView  pUsername;
    private TextView  pUserScreenName;
    private TextView  pLocation;
    private TextView  followingCount;
    private TextView  followerCount;
    private TextView  pUserBio;
    private TextView  pLikes;
    private TextView  pReplies;
    private TextView  pTweetsLabel;
    private TextView  pMedia;
    private ListView pTweetList;
    private ProfileTweetListAdapter adapter;
    private TwitterButtons twitterButtons;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        pUserImage = findViewById(R.id.pUserimage);
        twitterButtons = findViewById(R.id.twitterButtons4);
        pBanner = findViewById(R.id.pUserBanner);
        pUsername = findViewById(R.id.pUsername);
        pUserScreenName = findViewById(R.id.pUserScreenName);
        pLocation = findViewById(R.id.pLocation);
        followingCount = findViewById(R.id.followingCount);
        followerCount = findViewById(R.id.followerCount);
        pUserBio = findViewById(R.id.pUserBio);
        pLikes = findViewById(R.id.pLikes);
        pReplies = findViewById(R.id.pReplies);
        pTweetsLabel = findViewById(R.id.pTweetsLabel);
        pMedia = findViewById(R.id.pMedia);
        pTweetList = findViewById(R.id.pTweetlist);
        adapter = new ProfileTweetListAdapter(this,R.layout.tweet,TweetSampleDataProvider.profileTimeline);
        pUserImage.bringToFront();
        pUserImage.invalidate();
        Picasso.get().load(TweetSampleDataProvider.currentUser.getProfile_image_url()).transform(new TweetListAdapter.CircleTransform( )).into(pUserImage);
        Picasso.get().load(TweetSampleDataProvider.currentUser.getProfile_banner_url()).fit().centerCrop().into(pBanner);
        pUsername.setText(TweetSampleDataProvider.currentUser.getName());
        pUserScreenName.setText("@"+TweetSampleDataProvider.currentUser.getScreen_name());
        pUserBio.setText(TweetSampleDataProvider.currentUser.getDescription());
        pLocation.setText(TweetSampleDataProvider.currentUser.getLocation());
        followingCount.setText(String.valueOf(TweetSampleDataProvider.currentUser.getFriends_count()));
        followerCount.setText(String.valueOf(TweetSampleDataProvider.currentUser.getFollowers_count()));
        adapter.notifyDataSetChanged();

        pTweetList.setAdapter(adapter);
    }
}
