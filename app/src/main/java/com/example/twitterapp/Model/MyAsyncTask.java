package com.example.twitterapp.Model;

import android.os.AsyncTask;
import android.util.Log;


/**
 * Created by yang- on 11/06/2018.
 */

public class MyAsyncTask extends AsyncTask<String, Double, Status> {
    private final String TAG = "MyAsyncTask";

    @Override
    protected com.example.twitterapp.Model.Status doInBackground(String... strings) {
        OpenAuthentication openAuthentication = new OpenAuthentication();
        String textOAuth = openAuthentication.getOAuth();
        Log.d(TAG, textOAuth);
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(com.example.twitterapp.Model.Status status) {
        super.onPostExecute(status);
    }

    @Override
    protected void onProgressUpdate(Double... values) {
        super.onProgressUpdate(values);
    }
}
