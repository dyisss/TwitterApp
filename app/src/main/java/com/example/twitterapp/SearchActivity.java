package com.example.twitterapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;

import com.example.twitterapp.Adapters.TweetListAdapter;
import com.example.twitterapp.View.AuthenticationWebView;
import com.example.twitterapp.Model.OpenAuthentication;
import com.example.twitterapp.Model.Tweet;
import com.example.twitterapp.Model.TweetSampleDataProvider;
import com.example.twitterapp.View.TwitterButtons;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth10aService;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by yang- on 01/07/2018.
 */

public class SearchActivity extends Activity implements Observer{
    private ListView tweetList;
    private final String TAG = "mainactivity";
    public static ArrayList<Tweet> tweetslist = TweetSampleDataProvider.tweetsSearched ;
    private TweetListAdapter tweetListAdapter;


    //imageView
    private ImageView mSearchBtn;
    private ImageView userImage;
    private ImageView mPost;
    private TwitterButtons twitterButtons;
    //searchView
    private SearchView svSearch;

    //Twitter Activities fields
    private OAuthRequest request;
    private OAuth10aService service;
    private String res = null;

    final OpenAuthentication authentication = OpenAuthentication.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);
        authentication.addObserver(this);
        tweetList = findViewById(R.id.tweetsList);
        userImage = findViewById(R.id.acUserimage);
        mSearchBtn = findViewById(R.id.searchBtn);
        svSearch = findViewById(R.id.svSearch);
        tweetListAdapter = new TweetListAdapter(this, R.layout.tweet, tweetslist);
        tweetList.setAdapter(tweetListAdapter);
        service= authentication.getService();

        svSearch.setOnQueryTextListener(
                new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String s) {
                        searchTweets(s);
                        TweetListAdapter tweetListSearchedAdapter = new TweetListAdapter(SearchActivity.this, R.layout.tweet, TweetSampleDataProvider.tweetsSearched);
                        tweetList.setAdapter(tweetListSearchedAdapter);
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String s) {
                        searchTweets(s);
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

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    private class getSearchedTweetsTask extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            searchChanged();
        }

        @SuppressLint("NewApi")
        @Override
        protected Void doInBackground(String... strings) {

            String baseUrl = "https://api.twitter.com/1.1/search/tweets.json?count=50&q=";

            try{
                String encodedSearchTerm = URLEncoder.encode(strings[0], "utf-8");
                baseUrl += encodedSearchTerm;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            request = new OAuthRequest(Verb.GET, baseUrl);
            service.signRequest(authentication.getAccessToken(), request);
            Response response = null;
            try {
                response = service.execute(request);
                if (response.isSuccessful()){
                    res = response.getBody();
                    Log.d("response",res);
                }
                TweetSampleDataProvider.tweetsSearched.clear();
                TweetSampleDataProvider.parseJSONData(res,TweetSampleDataProvider.tweetsSearched);
            }catch (Exception e) {
                Log.d(TAG, e.toString()) ;
            }
            return null;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    public void searchTweets(String query){
        getSearchedTweetsTask get = new getSearchedTweetsTask();
        get.execute(query);
    }

    private void searchChanged(){
        tweetListAdapter.notifyDataSetChanged();
        tweetList.invalidate();
    }
}
