package com.example.twitterapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.twitterapp.Adapters.MentionsListAdapter;
import com.example.twitterapp.Adapters.TweetListAdapter;
import com.example.twitterapp.Model.Tweet;
import com.example.twitterapp.Model.TweetSampleDataProvider;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Kyle on 30-Jun-18.
 */

public class Alerts extends Activity {
    private ImageView aUserImage;
    private ListView aNotificationList;
    private MentionsListAdapter adapter;
    private ArrayList<Tweet>list=TweetSampleDataProvider.mentionTimeline;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alert);
        aUserImage = findViewById(R.id.aUserImage);
        aNotificationList = findViewById(R.id.aNotifications);
        adapter = new MentionsListAdapter(this,R.layout.tweet,list);
        aNotificationList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        aNotificationList.invalidate();
        if (TweetSampleDataProvider.currentUser != null) {
            Picasso.get().load(TweetSampleDataProvider.currentUser.getProfile_image_url()).transform(new TweetListAdapter.CircleTransform()).into(aUserImage);
            aUserImage.invalidate();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
        aNotificationList.invalidate();
    }
}
