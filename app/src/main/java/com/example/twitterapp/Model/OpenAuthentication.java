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

    public String getUrl() throws Exception{
        requestToken = service.getRequestToken();
        String authUrl = service.getAuthorizationUrl(requestToken);
        return authUrl;
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

    //Creates access token and sets authorized
    public void loggedIn_AccessToken(String str_accesstoken,String str_access_token_secret){

        this.str_accesstoken = str_accesstoken;
        this.str_access_token_secret = str_access_token_secret;

        accessToken = new OAuth1AccessToken(str_accesstoken ,str_access_token_secret);
        authorized=true;
    }

    public OAuth10aService getService() {
        return service;
    }

    public OAuth1AccessToken getAccessToken() {
        return accessToken;
    }

    public void setAuthorized(boolean authorized) {
        this.authorized = authorized;
    }

    public boolean isAuthorized() {
        return authorized;
    }


    public String getStr_accesstoken() {
        return str_accesstoken;
    }

    public String getStr_access_token_secret() {
        return str_access_token_secret;
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

    public void retweet(String id){
        setRetweetTask task = new setRetweetTask();
        task.execute(id);
    }

    public void unRetweet(String id){
        setUnRetweetTask task = new setUnRetweetTask();
        task.execute(id);
    }

    public void unlike(String id){
        setUnLikeTask task = new setUnLikeTask();
        task.execute(id);
    }

    public void like(String id){
        setLikeTask task = new setLikeTask();
        task.execute(id);
    }
    public void postTweet(String text){
        setPostTweet post = new setPostTweet();
        post.execute(text);
    }
}
