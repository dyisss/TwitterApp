package com.example.twitterapp.Model;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.github.scribejava.apis.TwitterApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.exceptions.OAuthException;
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuth1RequestToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth10aService;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Observable;

/**
 * Created by yang- on 13/06/2018.
 */

public class OpenAuthentication extends Observable {
    final public static String TAG = "OpenAuthentication";
    private static OpenAuthentication oAuth;
    private OAuth10aService service;
    private OAuth1RequestToken requestToken;
    private OAuth1AccessToken accessToken;
    private final static String API_KEY = "EnqIn4E2YDcSuONFOcrj1yNwL";
    private final static String API_SECRET="23cwrKzOTNPCcumdaNQ9x7GZTqaWceaFqCrqrQoUbmY14dCpvR";
    private boolean authorized = false;
    private String str_accesstoken;
    private String str_access_token_secret;
    private OAuthRequest request;
    private String res = null;
    private String TwitterStreamUrl = "https://api.twitter.com/1.1/statuses/user_timeline.json?screen_name=";
    private String screenUserName = "BattousaiDyis";

    private OpenAuthentication() {
        service = new ServiceBuilder(API_KEY)
                .apiSecret(API_SECRET)
                .callback("https://www.google.com/")
                .build(TwitterApi.instance());

    }

    public static OpenAuthentication getInstance(){
        if (oAuth == null){
            oAuth = new OpenAuthentication();
        }

        return oAuth;
    }

    public String getUrl(){
        try{
            requestToken = service.getRequestToken();
        }catch (Exception e){
            Log.d(TAG, e.toString());
        }
        String authUrl = service.getAuthorizationUrl(requestToken);
        return authUrl;
    }

    public void loggedIn_AccessToken(String str_accesstoken,String str_access_token_secret){

        this.str_accesstoken = str_accesstoken;
        this.str_access_token_secret = str_access_token_secret;

        accessToken = new OAuth1AccessToken(str_accesstoken ,str_access_token_secret);
        authorized=true;
    }

    public void setAccessToken(String verifier){
        try{
            this.accessToken = service.getAccessToken(requestToken, verifier);
            setAuthorized(true);
            this.str_accesstoken = accessToken.getToken();
            this.str_access_token_secret =accessToken.getTokenSecret();
        }catch (OAuthException e){
            setAuthorized(false);
        }catch (Exception e){
            Log.d(TAG, e.toString());
        }
    }

    public void setAuthorized(boolean authorized) {
        this.authorized = authorized;
    }

    public boolean isAuthorized() {
        return authorized;
    }

    public void callTweetTask(){
        getTweetsTask g = new getTweetsTask();
        g.execute();
    }

    public String getStr_accesstoken() {
        return str_accesstoken;
    }

    public String getStr_access_token_secret() {
        return str_access_token_secret;
    }

    @SuppressLint("StaticFieldLeak")
    private  class getTweetsTask extends AsyncTask<Void, Void, Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
          //  DownloadTwitterTask dt = new DownloadTwitterTask();
            // dt.execute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            notifyObservers();
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected Void doInBackground(Void... voids) {
            request = new OAuthRequest(Verb.GET, "https://api.twitter.com/1.1/statuses/home_timeline.json");
            service.signRequest(accessToken , request); // the access token from step 4
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
    private class getSearchedTweetsTask extends AsyncTask<String, Void, Void>{

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
            service.signRequest(accessToken , request);
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
        protected Void doInBackground(String... strings) {
            String url = "https://api.twitter.com/1.1/statuses/update.json?status=";
            String input = strings[0];
            input = input.replace(" ","%20");
            input = input.replace("#","%23");

            request = new OAuthRequest(Verb.POST,url+input);
            service.signRequest(accessToken,request);
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
            //  DownloadTwitterTask dt = new DownloadTwitterTask();
            // dt.execute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            notifyObservers();
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected Void doInBackground(Void... voids) {
            request = new OAuthRequest(Verb.GET, "https://api.twitter.com/1.1/account/verify_credentials.json");
            service.signRequest(accessToken , request); // the access token from step 4
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

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    public void searchTweets(String query){
        getSearchedTweetsTask get = new getSearchedTweetsTask();
        get.execute(query);
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
            //  DownloadTwitterTask dt = new DownloadTwitterTask();
            // dt.execute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            notifyObservers();
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected Void doInBackground(Void... voids) {
            request = new OAuthRequest(Verb.GET, "https://api.twitter.com/1.1/statuses/user_timeline.json");
            service.signRequest(accessToken, request); // the access token from step 4
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
            //  DownloadTwitterTask dt = new DownloadTwitterTask();
            // dt.execute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            notifyObservers();
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected Void doInBackground(Void... voids) {
            request = new OAuthRequest(Verb.GET, "https://api.twitter.com/1.1/statuses/mentions_timeline.json");
            service.signRequest(accessToken, request); // the access token from step 4
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

    private class setRetweetTask extends AsyncTask<String,Void,Void>{

        @Override
        protected Void doInBackground(String... strings) {
            String url = "https://api.twitter.com/1.1/statuses/retweet/:id.json";
            String input = strings[0];
            url = url.replace(":id",input);
            request = new OAuthRequest(Verb.POST,url);
            service.signRequest(accessToken,request);
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
    private class setUnRetweetTask extends AsyncTask<String,Void,Void>{

        @Override
        protected Void doInBackground(String... strings) {
            String url = "https://api.twitter.com/1.1/statuses/unretweet/:id.json";
            String input = strings[0];
            url = url.replace(":id",input);
            request = new OAuthRequest(Verb.POST,url);
            service.signRequest(accessToken,request);
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

    public void retweet(String id){
        setRetweetTask task = new setRetweetTask();
        task.execute(id);
    }
    public void unRetweet(String id){
        setUnRetweetTask task = new setUnRetweetTask();
        task.execute(id);
    }

    public void like(String id){
        setLikeTask task = new setLikeTask();
        task.execute(id);
    }

    private class setLikeTask extends AsyncTask<String,Void,Void>{

        @Override
        protected Void doInBackground(String... strings) {
            String url = "https://api.twitter.com/1.1/favorites/create.json?id=like";
            String input = strings[0];
            url = url.replace("like",input);
            request = new OAuthRequest(Verb.POST,url);
            service.signRequest(accessToken,request);
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

    public void unlike(String id){
        setUnLikeTask task = new setUnLikeTask();
        task.execute(id);
    }
    private class setUnLikeTask extends AsyncTask<String,Void,Void>{

        @Override
        protected Void doInBackground(String... strings) {
            String url = "https://api.twitter.com/1.1/favorites/destroy.json?id=like";
            String input = strings[0];
            url = url.replace("like",input);
            request = new OAuthRequest(Verb.POST,url);
            service.signRequest(accessToken,request);
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

    public void friendList(String screenUserName){
        getFriendList task = new getFriendList();
        task.execute(screenUserName);
    }

    private class getFriendList extends AsyncTask<String,Void,Void>{

        @Override
        protected Void doInBackground(String... strings) {
            String url = "https://api.twitter.com/1.1/followers/list.json?cursor=-1&screen_name=twitterdev&skip_status=true&include_user_entities=false";
            String input = strings[0];
            url = url.replace("twitterdev",input);
            request = new OAuthRequest(Verb.GET,url);
            service.signRequest(accessToken,request);
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

}
