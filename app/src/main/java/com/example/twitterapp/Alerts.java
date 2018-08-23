package com.example.twitterapp;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.twitterapp.Adapters.MentionsListAdapter;
import com.example.twitterapp.Adapters.TweetListAdapter;
import com.example.twitterapp.Model.Tweet;
import com.example.twitterapp.Model.TweetSampleDataProvider;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth10aService;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.example.twitterapp.MainActivity.authentication;

/**
 * Created by Kyle on 30-Jun-18.
 */

public class Alerts extends Activity {
    private static final String TAG = "Mentions" ;
    private ImageView aUserImage;
    private ListView mentionList;
    private MentionsListAdapter adapter;
    private ArrayList<Tweet>list=TweetSampleDataProvider.mentionTimeline;

    //Twitter Activities fields
    private OAuthRequest request;
    private OAuth10aService service;
    private String res = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alert);
        aUserImage = findViewById(R.id.aUserImage);
        mentionList = findViewById(R.id.aNotifications);
        adapter = new MentionsListAdapter(this,R.layout.tweet,list);
        mentionList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        mentionList.invalidate();
        if (TweetSampleDataProvider.currentUser != null) {
            Picasso.get().load(TweetSampleDataProvider.currentUser.getProfile_image_url()).transform(new TweetListAdapter.CircleTransform()).into(aUserImage);
            aUserImage.invalidate();
        }
        service= authentication.getService();
        callMentionsTimeline();
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
        mentionList.invalidate();
    }

    private class getMentionTimeline extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mentionTimelineChanged();
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

    private void mentionTimelineChanged(){
        adapter.notifyDataSetChanged();
        mentionList.invalidate();
    }
}
