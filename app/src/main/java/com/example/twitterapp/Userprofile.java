package com.example.twitterapp;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.twitterapp.Adapters.ProfileTweetListAdapter;
import com.example.twitterapp.Adapters.TweetListAdapter;
import com.example.twitterapp.Model.TweetSampleDataProvider;
import com.example.twitterapp.View.TwitterButtons;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth10aService;
import com.squareup.picasso.Picasso;

import static com.example.twitterapp.MainActivity.authentication;

/**
 * Created by Kyle on 08-Jun-18.
 */

public class Userprofile extends Activity {
    private static final String TAG = "Userprofile";
    private ImageView pUserImage;
    private ImageView pBanner;
    private TextView  pUsername;
    private TextView  pUserScreenName;
    private TextView  pLocation;
    private TextView  followingCount;
    private TextView  followerCount;
    private TextView  pUserBio;
    private TextView  pLikes;
    private TextView  pTweetsLabel;
    private ListView pTweetList;
    private ProfileTweetListAdapter adapter;
    private ImageView post;
    private TextView friendlist;

    //Twitter Activities fields
    private OAuthRequest request;
    private OAuth10aService service;
    private String res = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        friendlist = findViewById(R.id.followerLabel);
        friendlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Userprofile.this,FollowerList.class);
                startActivity(intent);
            }
        });
        pUserImage = findViewById(R.id.pUserimage);
        pBanner = findViewById(R.id.pUserBanner);
        pUsername = findViewById(R.id.pUsername);
        pUserScreenName = findViewById(R.id.pUserScreenName);
        pLocation = findViewById(R.id.pLocation);
        followingCount = findViewById(R.id.followingCount);
        followerCount = findViewById(R.id.followerCount);
        pUserBio = findViewById(R.id.pUserBio);
        pLikes = findViewById(R.id.pLikes);
        pTweetsLabel = findViewById(R.id.pTweetsLabel);
        pTweetList = findViewById(R.id.pTweetlist);
        adapter = new ProfileTweetListAdapter(this,R.layout.tweet,TweetSampleDataProvider.profileTimeline);
        pUserImage.bringToFront();
        pUserImage.invalidate();
        Picasso.get().load(TweetSampleDataProvider.currentUser.getProfile_image_url()).transform(new TweetListAdapter.CircleTransform( )).into(pUserImage);
        Picasso.get().load(TweetSampleDataProvider.currentUser.getProfile_banner_url()).fit().centerCrop().into(pBanner);
        pUsername.setText(TweetSampleDataProvider.currentUser.getName());
        pUserScreenName.setText(String.format("@%s", TweetSampleDataProvider.currentUser.getScreen_name()));
        pUserBio.setText(TweetSampleDataProvider.currentUser.getDescription());
        pLocation.setText(TweetSampleDataProvider.currentUser.getLocation());
        followingCount.setText(String.valueOf(TweetSampleDataProvider.currentUser.getFriends_count()));
        followerCount.setText(String.valueOf(TweetSampleDataProvider.currentUser.getFollowers_count()));
        adapter.notifyDataSetChanged();
        pTweetList.setAdapter(adapter);
        service= authentication.getService();

        //Gets users retweeted timeline and favorite timeline
        callProfileTimeline();
        FavoriteTimeline();

        pTweetsLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter = new ProfileTweetListAdapter(Userprofile.this,R.layout.tweet,TweetSampleDataProvider.profileTimeline);
                pTweetList.setAdapter(adapter);
                pTweetList.invalidate();
            }
        });

        pLikes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter = new ProfileTweetListAdapter(Userprofile.this,R.layout.tweet,TweetSampleDataProvider.favoriteTimeline);
                pTweetList.setAdapter(adapter);
                pTweetList.invalidate();
            }
        });
        post = findViewById(R.id.pPost);
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText edittext = new EditText(getBaseContext());
                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(Userprofile.this);
                builder.setTitle("Post a Tweet.")
                        .setMessage("Please enter up to 140 characters.")
                        .setView(edittext)
                        .setPositiveButton("Post", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Editable YouEditTextValue = edittext.getText();
                                while(YouEditTextValue.length()>140){
                                    Toast.makeText(Userprofile.this,"Character limit passed",Toast.LENGTH_SHORT).show();
                                }
                                String text = String.valueOf(YouEditTextValue);
                                authentication.postTweet(text);

                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                        .show();
            }
        });

        pUserImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.support.v7.app.AlertDialog.Builder ImageDialog = new AlertDialog.Builder(Userprofile.this);
                ImageView showImage = new ImageView(Userprofile.this);
                Picasso.get().load(TweetSampleDataProvider.currentUser.getProfile_image_url()).transform(new TweetListAdapter.CircleTransform( )).into(showImage);
                ImageDialog.setView(showImage);

                ImageDialog.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                });
               ImageDialog.show();
            }
            });
    }

    private class getProfileTimeline extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Profiledatachanged();
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected Void doInBackground(Void... voids) {
            request = new OAuthRequest(Verb.GET, "https://api.twitter.com/1.1/statuses/user_timeline.json");
            service.signRequest(authentication.getAccessToken(), request); // the access token from step 4
            Response response = null;
            try {
                response = service.execute(request);
                if (response.isSuccessful()) {
                    res = response.getBody();
                    Log.d("response", res);
                }
                TweetSampleDataProvider.profileTimeline.clear();
                TweetSampleDataProvider.parseProfileTimelineData("{\"statuses\":" + res + "}", TweetSampleDataProvider.profileTimeline);
            } catch (Exception e) {
                Log.d(TAG, e.toString());
            }
            return null;
        }
    }

    public void callProfileTimeline(){
        getProfileTimeline time = new getProfileTimeline();
        time.execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
        pTweetList.invalidate();
    }

    private void Profiledatachanged(){
        adapter.notifyDataSetChanged();
        pTweetList.invalidate();
    }

    public void FavoriteTimeline(){
        getFavoriteTimeline task = new getFavoriteTimeline();
        task.execute();
    }

    private class getFavoriteTimeline extends  AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Profiledatachanged();
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected Void doInBackground(Void... voids) {
            request = new OAuthRequest(Verb.GET, "https://api.twitter.com/1.1/favorites/list.json");
            service.signRequest(authentication.getAccessToken(), request); // the access token from step 4
            Response response = null;
            try {
                response = service.execute(request);
                if (response.isSuccessful()) {
                    res = response.getBody();
                    Log.d("response", res);
                }
                TweetSampleDataProvider.favoriteTimeline.clear();
                TweetSampleDataProvider.parseFavoriteTimeline("{\"statuses\":" + res + "}", TweetSampleDataProvider.favoriteTimeline);
            } catch (Exception e) {
                Log.d(TAG, e.toString());
            }
            return null;
        }
    }
}
