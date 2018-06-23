package com.example.twitterapp.Model;

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

/**
 * Created by yang- on 13/06/2018.
 */

public class OpenAuthentication {
    final public static String TAG = "OpenAuthentication";
    private static OpenAuthentication oAuth;
    private OAuth10aService service;
    private OAuth1RequestToken requestToken;
    private OAuth1AccessToken accessToken;
    private boolean authorized;
    private OAuthRequest request;

    private OpenAuthentication() {
        service = new ServiceBuilder("EnqIn4E2YDcSuONFOcrj1yNwL")
                .apiSecret("23cwrKzOTNPCcumdaNQ9x7GZTqaWceaFqCrqrQoUbmY14dCpvR")
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

    public void setAccessToken(String verifier){
        try{
            accessToken = service.getAccessToken(requestToken, verifier);

        }catch (OAuthException e){
            setAuthorized(false);
        }catch (Exception e){
            Log.d(TAG, e.toString());
        }
    }

    public void setAuthorized(boolean authorized) {
        this.authorized = authorized;
    }

    private  class getTweetsTask extends AsyncTask<Void, Void, Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected Void doInBackground(Void... voids) {
            Response response = null;
            String res = null;
            request = new OAuthRequest(Verb.GET, "https://api.twitter.com/1.1/account/verify_credentials.json");
            service.signRequest(accessToken, request); // the access token from step 4
            try {
                response = service.execute(request);
                if (response.isSuccessful()){
                    res = response.getBody();
                }
                TweetSampleDataProvider.parseJSONData("{'statues'"+res+"}",TweetSampleDataProvider.tweetsTimeline);
            }catch (Exception e) {
                Log.d(TAG, e.toString()) ;
            }
        return null;
        }
    }
}
