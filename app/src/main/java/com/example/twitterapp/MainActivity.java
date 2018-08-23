package com.example.twitterapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.twitterapp.Adapters.TweetListAdapter;
import com.example.twitterapp.View.AuthenticationWebView;
import com.example.twitterapp.Model.OpenAuthentication;
import com.example.twitterapp.Model.Tweet;
import com.example.twitterapp.Model.TweetSampleDataProvider;
import com.example.twitterapp.Model.User;
import com.example.twitterapp.View.TwitterButtons;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth10aService;
import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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


    //Twitter Activities fields
    final public static String TAG = "MainActivity";
    public final static OpenAuthentication authentication = OpenAuthentication.getInstance();
    private OAuthRequest request;
    private OAuth10aService service;
    private String res = null;

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
        fillList(tweetslist);
        twitterButtons = findViewById(R.id.hTwitterButtons);
        SharedPreferences mSharedPreferences = getSharedPreferences(MainActivity.PREFS, Context.MODE_PRIVATE);
        authentication.setAuthorized(mSharedPreferences.getBoolean(AUTHORISED, false));
        if (authentication.isAuthorized()) {
            service= authentication.getService();
            authentication.loggedIn_AccessToken(mSharedPreferences.getString(ACCESS_TOKEN, ""),
                    mSharedPreferences.getString(ACCESS_TOKEN_SECRET, ""));
        }
        if (TweetSampleDataProvider.tweetsTimeline == null) {
            fillList(tweetslist);
                tweetListAdapter.notifyDataSetChanged();
                tweetList.invalidate();
            }

        tweetList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("detailclick", "fuck");

                Intent detailedTweet = new Intent(MainActivity.this,DetailedActivity.class);

                detailedTweet.putExtra("MainActivity",i);
                startActivity(detailedTweet);
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
        authentication.addObserver(this);
        tweetListAdapter.notifyDataSetChanged();
        tweetList.invalidate();
        if (authentication.isAuthorized()) {
            callTweetTask();
            callProfileTimeline();
            setUser();
            tweetListAdapter.notifyDataSetChanged();
            tweetList.invalidate();
            if (TweetSampleDataProvider.currentUser != null) {
                current = TweetSampleDataProvider.currentUser;
                Picasso.get().load(TweetSampleDataProvider.currentUser.getProfile_image_url()).transform(new TweetListAdapter.CircleTransform()).into(userImage);
                userImage.invalidate();
            }
        } else {
            authorisationIntent();
            fillList(tweetslist);
            tweetListAdapter.notifyDataSetChanged();
            tweetList.invalidate();
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
        authentication.setAuthorized(mSharedPreferences.getBoolean(AUTHORISED, false));
        mEditor.commit();
        authorisationIntent();

    }

    private void fillList(ArrayList<Tweet> tweets){
        tweetListAdapter = new TweetListAdapter(this, R.layout.tweet, tweets);
        tweetList.setAdapter(tweetListAdapter);
        tweetListAdapter.notifyDataSetChanged();
    }

    @SuppressLint("StaticFieldLeak")
    private  class getTweetsTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            datachanged();
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected Void doInBackground(Void... voids) {
            request = new OAuthRequest(Verb.GET, "https://api.twitter.com/1.1/statuses/home_timeline.json");
            service.signRequest(authentication.getAccessToken() , request); // the access token from step 4
            Response response = null;
            try {
                response = service.execute(request);
                if (response.isSuccessful()){
                    res = response.getBody();
                    Log.d("response",res);
                }
                TweetSampleDataProvider.tweetsTimeline.clear();
                TweetSampleDataProvider.parseJSONData("{\"statuses\":"+res+"}",TweetSampleDataProvider.tweetsTimeline);
            }catch (Exception e) {
                Log.d(TAG, e.toString()) ;
            }
            return null;
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class setCurrentUserTask extends AsyncTask<Void, Void, Void>{
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            datachanged();
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected Void doInBackground(Void... voids) {
            request = new OAuthRequest(Verb.GET, "https://api.twitter.com/1.1/account/verify_credentials.json");
            service.signRequest(authentication.getAccessToken() , request); // the access token from step 4
            Response response = null;
            try {
                response = service.execute(request);
                if (response.isSuccessful()){
                    res = response.getBody();
                    Log.d("response",res);
                }
                TweetSampleDataProvider.setCurrentUser("{\"user\":"+res+"}");
            }catch (Exception e) {
                Log.d(TAG, e.toString()) ;
            }
            return null;
        }

    }

    private class getProfileTimeline extends  AsyncTask<Void, Void, Void> {
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            datachanged();
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected Void doInBackground(Void... voids) {
            request = new OAuthRequest(Verb.GET, "https://api.twitter.com/1.1/statuses/user_timeline.json");
            service.signRequest(authentication.getAccessToken(), request); // the access token from step 4
            Response response = null;
            try {
                response = service.execute(request);
                if (response.isSuccessful()) {
                    res = response.getBody();
                    Log.d("response", res);
                }
                TweetSampleDataProvider.profileTimeline.clear();
                TweetSampleDataProvider.parseProfileTimelineData("{\"statuses\":" + res + "}", TweetSampleDataProvider.profileTimeline);
            } catch (Exception e) {
                Log.d(TAG, e.toString());
            }
            return null;
        }
    }

    public void setUser(){
        setCurrentUserTask user = new setCurrentUserTask();
        user.execute();
    }

    public void callProfileTimeline(){
        getProfileTimeline time = new getProfileTimeline();
        time.execute();
    }

    private void datachanged(){
        tweetListAdapter.notifyDataSetChanged();
        tweetList.invalidate();
    }

    public void callTweetTask(){
        getTweetsTask g = new getTweetsTask();
        g.execute();
    }
}




