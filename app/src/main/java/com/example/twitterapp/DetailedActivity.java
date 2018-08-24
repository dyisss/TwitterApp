package com.example.twitterapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.twitterapp.Adapters.TweetListAdapter;
import com.example.twitterapp.View.AuthenticationWebView;
import com.example.twitterapp.Model.OpenAuthentication;
import com.example.twitterapp.Model.Tweet;
import com.example.twitterapp.Model.TweetSampleDataProvider;
import com.example.twitterapp.Model.User;
import com.example.twitterapp.View.TweetView;
import com.example.twitterapp.View.TwitterButtons;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth10aService;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import static com.example.twitterapp.MainActivity.authentication;

/**
 * Created by yang- on 01/07/2018.
 */

public class DetailedActivity extends Activity implements Observer {
    final public static String TAG = "OpenAuthentication";
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
    private TweetView tweetView;

    //current user
    public static User current;
    public static String userid;

    //Twitter Activities fields
    private OAuthRequest request;
    private OAuth10aService service;
    private String res = null;


    public final static OpenAuthentication authentication = OpenAuthentication.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailed_tweet);
        authentication.addObserver(this);
        tweetList = findViewById(R.id.detailedtweetlist);
        userImage = findViewById(R.id.acUserimage);
        mSearchBtn = findViewById(R.id.searchBtn);
        tweetView = findViewById(R.id.tweetView);

        int index = getIntent().getIntExtra("MainActivity", -1);
        Tweet tweet = tweetslist.get(index);
        service= authentication.getService();

//        Picasso.get().load().transform(new TweetListAdapter.CircleTransform( )).into(userImage);
        tweetView.setTweet(tweet.getText());
        tweetView.setTweetUsername(tweet.getUser().getScreen_name());
        tweetView.setTweetName(tweet.getUser().getName());

        tweetReactions(tweetslist.get(index).getId());
        tweetListAdapter = new TweetListAdapter(this, R.layout.tweet, TweetSampleDataProvider.tweetsDetailed);
        tweetList.setAdapter(tweetListAdapter);
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

    private class getRetweetsReactionsTask extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            tweetListAdapter.notifyDataSetChanged();
        }

        @SuppressLint("NewApi")
        @Override
        protected Void doInBackground(String... strings) {
            request = new OAuthRequest(Verb.GET, "https://api.twitter.com/1.1/statuses/retweets/"+ strings[0] +".json");
            service.signRequest(authentication.getAccessToken() , request);
            Response response = null;
            try {
                response = service.execute(request);
                if (response.isSuccessful()){
                    res = response.getBody();
                    Log.d("response",res);
                }
                TweetSampleDataProvider.tweetsDetailed.clear();
                TweetSampleDataProvider.parseJSONData(res,TweetSampleDataProvider.tweetsDetailed);
            }catch (Exception e) {
                Log.d(TAG, e.toString()) ;
            }
            return null;
        }
    }

    public void tweetReactions(String id){
        DetailedActivity.getRetweetsReactionsTask get = new DetailedActivity.getRetweetsReactionsTask();
        get.execute(id);
    }
}
