package com.example.twitterapp.View;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.twitterapp.Model.OpenAuthentication;
import com.example.twitterapp.R;

import java.io.IOException;

/**
 * Created by Kyle on 22-Jun-18.
 */

public class AuthenticationWebView extends AppCompatActivity{
    private WebView wvAuth ;
    final OpenAuthentication openAuthentication = OpenAuthentication.getInstance();
    private String verifier;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.authroization);
        wvAuth = findViewById(R.id.tweetWeb);
        wvAuth.setWebViewClient(new WebViewClient(){

            public boolean shouldOverrideUrlLoading(WebView view, String url) throws RuntimeException {
                if(url.startsWith("https://www.google.com/")){
                    Uri uri = Uri.parse(url);
                    verifier = uri.getQueryParameter("oauth_verifier");
                    AccessTokenTask accessTokenTask = new AccessTokenTask();
                    accessTokenTask.execute(verifier);
                }
           return true;
            }
        });
        AuthorisationUrlTask authorisationUrlTask = new AuthorisationUrlTask();
        authorisationUrlTask.execute();
    }

    @SuppressLint("StaticFieldLeak")
    private class AuthorisationUrlTask extends AsyncTask<Void, Void, String>{
        @Override
        protected void onPostExecute(String result) {
            wvAuth.loadUrl(result);
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                return openAuthentication.getUrl();
            } catch (Exception e){
                return e.toString();
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class AccessTokenTask extends AsyncTask<String, Void, Void>{
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            AuthenticationWebView.this.finish();
        }

        @Override
        protected Void doInBackground(String... strings) {
            if(strings != null) {

                openAuthentication.setAccessToken(strings[0]);
            }else{
                Log.d("Tag","strings is empty wtf");
            }
            return null;
        }
    }
}
