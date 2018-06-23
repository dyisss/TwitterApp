package com.example.twitterapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.twitterapp.Adapters.TweetListAdapter;
import com.example.twitterapp.Model.AuthenticationWebView;
import com.example.twitterapp.Model.OpenAuthentication;
import com.example.twitterapp.Model.SearchMetaData;
import com.example.twitterapp.Model.Status;
import com.example.twitterapp.Model.Tweet;
import com.example.twitterapp.Model.TweetSampleDataProvider;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class MainActivity extends AppCompatActivity implements Observer {
    private ListView tweetList;
    private final String TAG = "mainactivity";
    public static ArrayList<Tweet> tweetslist = TweetSampleDataProvider.tweetsTimeline;
    private TweetListAdapter tweetListAdapter ;

    final OpenAuthentication authentication = OpenAuthentication.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        authorisationIntent();
        tweetListAdapter = new TweetListAdapter(this,R.layout.tweet,tweetslist);
        tweetList = findViewById(R.id.tweetList);
        tweetList.setAdapter(tweetListAdapter);
        tweetList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Intent detailedTweet = new Intent(MainActivity.this,DetailedTweet.class);
//                detailedTweet.putExtra(INDEX,i);
//                detailedTweet.putExtra(TAG,"MainActivity");
//                startActivity(detailedTweet);
            }
        });
    }

    public void authorisationIntent(){
        Intent authIntent = new Intent(MainActivity.this, AuthenticationWebView.class);
        startActivity(authIntent);
    }

    @Override
    public void update(Observable observable, Object o) {
        tweetListAdapter.notifyDataSetChanged();
        tweetList.invalidate();
    }
}



