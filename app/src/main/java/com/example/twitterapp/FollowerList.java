package com.example.twitterapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.twitterapp.Adapters.FollowerListAdapter;
import com.example.twitterapp.Adapters.TweetListAdapter;
import com.example.twitterapp.Model.TweetSampleDataProvider;
import com.example.twitterapp.Model.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Kyle on 01-Jul-18.
 */

public class FollowerList extends Activity {
    private ImageView profileimage;
    private ListView followerList;
    private FollowerListAdapter adapter;
    private ArrayList<User>Followers = TweetSampleDataProvider.userFollowers;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.follower);
        profileimage=findViewById(R.id.flProfile);
        followerList = findViewById(R.id.followerlist);
        adapter = new FollowerListAdapter(this,R.layout.user, Followers);
        followerList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        followerList.invalidate();
        Picasso.get().load(MainActivity.current.getProfile_image_url()).transform(new TweetListAdapter.CircleTransform( )).into(profileimage);
        profileimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
        followerList.invalidate();;
    }
}
