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
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
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
                                    postTweet(text);

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
            callMentionsTimeline();
            setUser();
            FavoriteTimeline();
            tweetListAdapter.notifyDataSetChanged();
            tweetList.invalidate();
            if (TweetSampleDataProvider.currentUser != null) {
                current = TweetSampleDataProvider.currentUser;
                Picasso.get().load(TweetSampleDataProvider.currentUser.getProfile_image_url()).transform(new TweetListAdapter.CircleTransform()).into(userImage);
                userImage.invalidate();
                callMentionsTimeline();
                friendList(current.getScreen_name());
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

    public void callTweetTask(){
        getTweetsTask g = new getTweetsTask();
        g.execute();
    }

    @SuppressLint("StaticFieldLeak")
    private  class getTweetsTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

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

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    private class getRetweetsReactionsTask extends AsyncTask<String, Void, Void>{
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            datachanged();
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

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    private class getSearchedTweetsTask extends AsyncTask<String, Void, Void>{

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            datachanged();
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
            service.signRequest(authentication.getAccessToken() , request);
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

    private class setPostTweet extends AsyncTask<String,Void,Void>{
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            datachanged();
        }

        @Override
        protected Void doInBackground(String... strings) {
            String url = "https://api.twitter.com/1.1/statuses/update.json?status=";
            String input = strings[0];
            input = input.replace(" ","%20");
            input = input.replace("#","%23");

            request = new OAuthRequest(Verb.POST,url+input);
            service.signRequest(authentication.getAccessToken(),request);
            Response response = null;
            try {
                response = service.execute(request);
                if (response.isSuccessful()) {
                    res = response.getBody();
                    Log.d("response", res);
                }
            }catch (Exception e) {
                Log.d(TAG, e.toString()) ;
            }

            return null;
        }
    }


    @SuppressLint("StaticFieldLeak")
    private class setCurrentUserTask extends AsyncTask<Void, Void, Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

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


    public void postTweet(String text){
        setPostTweet post = new setPostTweet();
        post.execute(text);
    }

    public void setUser(){
        setCurrentUserTask user = new setCurrentUserTask();
        user.execute();
    }

    private class getProfileTimeline extends  AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

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

    public void callProfileTimeline(){
        getProfileTimeline time = new getProfileTimeline();
        time.execute();
    }

    private class getMentionTimeline extends  AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            datachanged();
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected Void doInBackground(Void... voids) {
            request = new OAuthRequest(Verb.GET, "https://api.twitter.com/1.1/statuses/mentions_timeline.json");
            service.signRequest(authentication.getAccessToken(), request); // the access token from step 4
            Response response = null;
            try {
                response = service.execute(request);
                if (response.isSuccessful()) {
                    res = response.getBody();
                    Log.d("response", res);
                }
                TweetSampleDataProvider.mentionTimeline.clear();
                TweetSampleDataProvider.parseMentionTimelineData("{\"statuses\":" + res + "}", TweetSampleDataProvider.mentionTimeline);
            } catch (Exception e) {
                Log.d(TAG, e.toString());
            }
            return null;
        }
    }

    public void callMentionsTimeline(){
        getMentionTimeline timeline = new getMentionTimeline();
        timeline.execute();
    }


    public void friendList(String screenUserName){
        getFriendList task = new getFriendList();
        task.execute(screenUserName);
    }

    private class getFriendList extends AsyncTask<String,Void,Void>{
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            datachanged();
        }

        @Override
        protected Void doInBackground(String... strings) {
            String url = "https://api.twitter.com/1.1/followers/list.json?cursor=-1&screen_name=twitterdev&skip_status=true&include_user_entities=false";
            String input = strings[0];
            url = url.replace("twitterdev",input);
            request = new OAuthRequest(Verb.GET,url);
            service.signRequest(authentication.getAccessToken(),request);
            Response response = null;
            try {
                response = service.execute(request);
                if (response.isSuccessful()) {
                    res = response.getBody();
                    Log.d("response", res);
                }
                TweetSampleDataProvider.userFollowers.clear();
                TweetSampleDataProvider.parseUserFriendsData(res,TweetSampleDataProvider.userFollowers);
            }catch (Exception e) {
                Log.d(TAG, e.toString()) ;
            }

            return null;
        }
    }

    public void FavoriteTimeline(){
        getFavoriteTimeline task = new getFavoriteTimeline();
        task.execute();
    }

    private class getFavoriteTimeline extends  AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            datachanged();
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected Void doInBackground(Void... voids) {
            request = new OAuthRequest(Verb.GET, "https://api.twitter.com/1.1/favorites/list.json");
            service.signRequest(authentication.getAccessToken(), request); // the access token from step 4
            Response response = null;
            try {
                response = service.execute(request);
                if (response.isSuccessful()) {
                    res = response.getBody();
                    Log.d("response", res);
                }
                TweetSampleDataProvider.favoriteTimeline.clear();
                TweetSampleDataProvider.parseFavoriteTimeline("{\"statuses\":" + res + "}", TweetSampleDataProvider.favoriteTimeline);
            } catch (Exception e) {
                Log.d(TAG, e.toString());
            }
            return null;
        }
    }

    private void datachanged(){
        tweetListAdapter.notifyDataSetChanged();
        tweetList.invalidate();
    }
}




