package com.example.twitterapp.Model;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.twitterapp.R;

/**
 * Created by Kyle on 22-Jun-18.
 */

public class AuthenticationWebView extends AppCompatActivity{
    private WebView wvAuth ;
    final OpenAuthentication openAuthentication = OpenAuthentication.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.authroization);
        wvAuth = findViewById(R.id.tweetWeb);
        wvAuth.setWebViewClient(new WebViewClient(){

            public boolean shouldOverrideUrlLoading(WebView view, String url){
                if(url.startsWith("http://"));{
                    wvAuth.destroy();

                    AlertDialog.Builder tBuildinger = new AlertDialog.Builder(AuthenticationWebView.this);
                    AccessTokenTask accessTokenTask = new AccessTokenTask();
                    accessTokenTask.execute();
                }

           return false;
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
            return openAuthentication.getUrl();
        }
    }

    private class AccessTokenTask extends AsyncTask<String, Void, Void>{
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            AuthenticationWebView.this.finish();
        }

        @Override
        protected Void doInBackground(String... strings) {
            openAuthentication.setAccessToken(strings[0]);
            return null;
        }
    }
}
