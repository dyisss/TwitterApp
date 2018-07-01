package com.example.twitterapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

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

public class MainActivity extends Activity implements Observer {
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //setting up Timeline
        authentication.addObserver(this);
        tweetList = findViewById(R.id.tweetsList);
        userImage = findViewById(R.id.acUserimage);
        mSearchBtn = findViewById(R.id.searchBtn);
        tweetList.invalidate();
        twitterButtons = findViewById(R.id.hTwitterButtons);
        tweetListAdapter = new TweetListAdapter(this, R.layout.tweet, tweetslist);
        tweetList.setAdapter(tweetListAdapter);
        if (TweetSampleDataProvider.tweetsTimeline != null) {

        fillList(tweetslist);

        if(TweetSampleDataProvider.tweetsTimeline!=null){
            tweetListAdapter.notifyDataSetChanged();
            tweetList.invalidate();
        }
        SharedPreferences mSharedPreferences = getSharedPreferences(MainActivity.PREFS, Context.MODE_PRIVATE);
        authentication.setAuthorized(mSharedPreferences.getBoolean(AUTHORISED,false));
        if (authentication.isAuthorized()){
            authentication.loggedIn_AccessToken(mSharedPreferences.getString(ACCESS_TOKEN, ""),
                    mSharedPreferences.getString(ACCESS_TOKEN_SECRET, ""));
        }
//        tweetList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Intent detailedTweet = new Intent(MainActivity.this,DetailedTweet.class);

//                detailedTweet.putExtra(INDEX,i);
//                detailedTweet.putExtra(TAG,"MainActivity");
//                startActivity(detailedTweet);
//            }
//        });

        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent searchIntent = new Intent(MainActivity.this,SearchActivity.class);
                startActivity(searchIntent);
            }
        });

        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //add intent to access user profile
                Intent intent = new Intent(MainActivity.this, Userprofile.class);
                startActivity(intent);
            }
        });
        mPost = findViewById(R.id.mPost);
        mPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText edittext = new EditText(getBaseContext());
                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Post a Tweet.")
                        .setMessage("Please enter up to 140 characters.")
                        .setView(edittext)
                        .setPositiveButton("Post", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Editable YouEditTextValue = edittext.getText();
                                while (YouEditTextValue.length() > 140) {
                                    Toast.makeText(MainActivity.this, "Character limit passed", Toast.LENGTH_SHORT).show();
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
                Intent searchIntent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(searchIntent);
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
        SharedPreferences mSharedPreferences = getSharedPreferences(MainActivity.PREFS, Context.MODE_PRIVATE);
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
        SharedPreferences mSharedPreferences = getSharedPreferences(MainActivity.PREFS, Context.MODE_PRIVATE);
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



