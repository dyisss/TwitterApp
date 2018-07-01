package com.example.twitterapp;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.twitterapp.Adapters.ProfileTweetListAdapter;
import com.example.twitterapp.Adapters.TweetListAdapter;
import com.example.twitterapp.Model.TweetSampleDataProvider;
import com.example.twitterapp.View.TwitterButtons;
import com.squareup.picasso.Picasso;

import static com.example.twitterapp.MainActivity.authentication;

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
    private TextView  pTweetsLabel;
    private ListView pTweetList;
    private ProfileTweetListAdapter adapter;
    private TwitterButtons twitterButtons;
    private ImageView post;
    private TextView friendlist;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        friendlist = findViewById(R.id.followerLabel);
        friendlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Userprofile.this,FollowerList.class);
                startActivity(intent);
            }
        });
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
        pTweetsLabel = findViewById(R.id.pTweetsLabel);
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

        pTweetsLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter = new ProfileTweetListAdapter(Userprofile.this,R.layout.tweet,TweetSampleDataProvider.profileTimeline);
                pTweetList.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                pTweetList.invalidate();
            }
        });

        pLikes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter = new ProfileTweetListAdapter(Userprofile.this,R.layout.tweet,TweetSampleDataProvider.favoriteTimeline);
                pTweetList.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                pTweetList.invalidate();
            }
        });
        post = findViewById(R.id.pPost);
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText edittext = new EditText(getBaseContext());
                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(Userprofile.this);
                builder.setTitle("Post a Tweet.")
                        .setMessage("Please enter up to 140 characters.")
                        .setView(edittext)
                        .setPositiveButton("Post", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Editable YouEditTextValue = edittext.getText();
                                while(YouEditTextValue.length()>140){
                                    Toast.makeText(Userprofile.this,"Character limit passed",Toast.LENGTH_SHORT).show();
                                }
                                String text = String.valueOf(YouEditTextValue);
                                authentication.postTweet(text);

                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                        .show();
            }
        });

        pUserImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.support.v7.app.AlertDialog.Builder ImageDialog = new AlertDialog.Builder(Userprofile.this);
                ImageView showImage = new ImageView(Userprofile.this);
                Picasso.get().load(TweetSampleDataProvider.currentUser.getProfile_image_url()).transform(new TweetListAdapter.CircleTransform( )).into(showImage);
                ImageDialog.setView(showImage);

                ImageDialog.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                });
               ImageDialog.show();
            }
            });
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
        pTweetList.invalidate();
    }

}
