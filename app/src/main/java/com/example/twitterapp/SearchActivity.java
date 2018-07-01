package com.example.twitterapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;

import com.example.twitterapp.Adapters.TweetListAdapter;
import com.example.twitterapp.Model.AuthenticationWebView;
import com.example.twitterapp.Model.OpenAuthentication;
import com.example.twitterapp.Model.Tweet;
import com.example.twitterapp.Model.TweetSampleDataProvider;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by yang- on 01/07/2018.
 */

public class SearchActivity extends AppCompatActivity implements Observer{
    private ListView tweetList;
    private final String TAG = "mainactivity";
    public static ArrayList<Tweet> tweetslist = TweetSampleDataProvider.tweetsSearched ;
    private TweetListAdapter tweetListAdapter;
    private String content;

    //ImageViews
    private ImageView homeBtn;
    private ImageView searchBtn;
    private ImageView messageBtn;
    private ImageView alertBtn;

    //searchView
    private SearchView svSearch;

    final OpenAuthentication authentication = OpenAuthentication.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);
        authentication.addObserver(this);
        tweetList = findViewById(R.id.tweetsList);
        svSearch = findViewById(R.id.svSearch);
        tweetListAdapter = new TweetListAdapter(this, R.layout.tweet, tweetslist);
        tweetList.setAdapter(tweetListAdapter);

        svSearch.setOnQueryTextListener(
                new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String s) {
                        authentication.searchTweets(s);
                        TweetListAdapter tweetListSearchedAdapter = new TweetListAdapter(SearchActivity.this, R.layout.tweet, TweetSampleDataProvider.tweetsSearched);
                        tweetList.setAdapter(tweetListSearchedAdapter);
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String s) {
                        authentication.searchTweets(s);
                        TweetListAdapter tweetListSearchedAdapter = new TweetListAdapter(SearchActivity.this, R.layout.tweet, TweetSampleDataProvider.tweetsSearched);
                        tweetList.setAdapter(tweetListSearchedAdapter);
                        return false;
                    }
                }
        );
    }

    public void authorisationIntent() {
        Intent authIntent = new Intent(SearchActivity.this, AuthenticationWebView.class);
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
