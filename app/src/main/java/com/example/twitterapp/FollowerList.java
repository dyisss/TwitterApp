package com.example.twitterapp;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.twitterapp.Adapters.FollowerListAdapter;
import com.example.twitterapp.Adapters.TweetListAdapter;
import com.example.twitterapp.Model.TweetSampleDataProvider;
import com.example.twitterapp.Model.User;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth10aService;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.example.twitterapp.MainActivity.authentication;
import static com.example.twitterapp.MainActivity.current;

/**
 * Created by Kyle on 01-Jul-18.
 */

public class FollowerList extends Activity {
    private static final String TAG ="FollowerList" ;
    private ImageView profileimage;
    private ListView followerList;
    private FollowerListAdapter adapter;
    private ArrayList<User>Followers = TweetSampleDataProvider.userFollowers;

    //Twitter Activities fields
    private OAuthRequest request;
    private OAuth10aService service;
    private String res = null;

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
        Picasso.get().load(current.getProfile_image_url()).transform(new TweetListAdapter.CircleTransform( )).into(profileimage);
        profileimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        service= authentication.getService();
        friendList(current.getScreen_name());
    }

    private class getFriendList extends AsyncTask<String,Void,Void> {
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            followerListChanged();
        }

        @Override
        protected Void doInBackground(String... strings) {
            String url = "https://api.twitter.com/1.1/followers/list.json?cursor=-1&screen_name=twitterdev&skip_status=true&include_user_entities=false";
            String input = strings[0];
            url = url.replace("twitterdev",input);
            request = new OAuthRequest(Verb.GET,url);
            service.signRequest(authentication.getAccessToken(),request);
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

    public void friendList(String screenUserName){
        getFriendList task = new getFriendList();
        task.execute(screenUserName);
    }

    private void followerListChanged(){
        adapter.notifyDataSetChanged();
        followerList.invalidate();
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
        followerList.invalidate();;
    }
}
