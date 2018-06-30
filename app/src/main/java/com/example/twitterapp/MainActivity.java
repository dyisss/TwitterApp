package com.example.twitterapp;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.twitterapp.Adapters.TweetListAdapter;
import com.example.twitterapp.Model.AuthenticationWebView;
import com.example.twitterapp.Model.OpenAuthentication;
import com.example.twitterapp.Model.SearchMetaData;
import com.example.twitterapp.Model.Status;
import com.example.twitterapp.Model.Tweet;
import com.example.twitterapp.Model.TweetSampleDataProvider;
import com.github.scribejava.core.model.OAuth1AccessToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class MainActivity extends AppCompatActivity implements Observer {
    private ListView tweetList;
    private final String TAG = "mainactivity";
    public static ArrayList<Tweet> tweetslist =TweetSampleDataProvider.tweetsTimeline ;
    private TweetListAdapter tweetListAdapter;

    //imageView
    public ImageView mHomeBtn = TwitterButtons.homeBtn;
    public ImageView mSearcBtn = TwitterButtons.searchBtn;
    public ImageView mMessageBtn = TwitterButtons.messageBtn;
    public ImageView mAlertBtn = TwitterButtons.alertBtn;
    private ImageView userImage;


    final OpenAuthentication authentication = OpenAuthentication.getInstance();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //setting up Timeline
        authentication.addObserver(this);
        tweetList = findViewById(R.id.tweetsList);
        userImage = findViewById(R.id.acUserimage);
        searchBtn = findViewById(R.id.searchBtn);
        authorisationIntent();
        tweetListAdapter = new TweetListAdapter(this, R.layout.tweet, tweetslist);
        tweetList.setAdapter(tweetListAdapter);
//        tweetList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Intent detailedTweet = new Intent(MainActivity.this,DetailedTweet.class);
//                detailedTweet.putExtra(INDEX,i);
//                detailedTweet.putExtra(TAG,"MainActivity");
//                startActivity(detailedTweet);
//            }
//        });
        mHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        mMessageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //add intent to access user profile
            }
        });
        mAlertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,Alerts.class);
                startActivity(intent);
            }
        });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                authentication.searchTweets("rachel");
            }
        });
    }


    public void authorisationIntent() {
        Intent authIntent = new Intent(MainActivity.this, AuthenticationWebView.class);
        startActivity(authIntent);
    }

    @Override
    public void update(Observable observable, Object o) {
        tweetListAdapter.notifyDataSetChanged();
        tweetList.invalidate();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (authentication.isAuthorized()) {
            authentication.addObserver(this);
            authentication.callTweetTask();
            tweetListAdapter.notifyDataSetChanged();
         //   Picasso.get().load();
            tweetList.invalidate();
        }else{
            authorisationIntent();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}



