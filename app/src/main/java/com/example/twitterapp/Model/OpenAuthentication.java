package com.example.twitterapp.Model;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.example.twitterapp.R;
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

    private class DownloadTwitterTask extends AsyncTask<String, Void, String> {
        final static String CONSUMER_KEY = "MY CONSUMER KEY";
        final static String CONSUMER_SECRET = "MY CONSUMER SECRET";
        final static String TwitterTokenURL = "https://api.twitter.com/oauth2/token";
        final static String TwitterStreamURL = "https://api.twitter.com/1.1/statuses/user_timeline.json?screen_name=";

        @Override
        protected String doInBackground(String... screenNames) {
            String result = null;

            if (screenNames.length > 0) {
                result = getTwitterStream(screenNames[0]);
                screenUserName = result;
            }
            return result;
        }

        private String getTwitterStream(String screenName) {
            return screenName;
        }
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

    private class getSearchedTweetsTask extends AsyncTask<String, Void, Void>{

        @SuppressLint("NewApi")
        @Override
        protected Void doInBackground(String... strings) {

            String url = "https://api.twitter.com/1.1/search/tweets.json?q=";
            for (int i = 0; i < strings.length; i++) {
                url += strings[i];

                if (i != strings.length - 1){
                    url += "&";
                }
            }

            request = new OAuthRequest(Verb.GET, url);
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

    public void searchTweets(String query){
        getSearchedTweetsTask get = new getSearchedTweetsTask();
        get.execute(query);
    }
}
