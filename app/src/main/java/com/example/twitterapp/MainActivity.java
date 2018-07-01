package com.example.twitterapp;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.twitterapp.Adapters.TweetListAdapter;
import com.example.twitterapp.Model.AuthenticationWebView;
import com.example.twitterapp.Model.OpenAuthentication;
import com.example.twitterapp.Model.SearchMetaData;
import com.example.twitterapp.Model.Status;
import com.example.twitterapp.Model.Tweet;
import com.example.twitterapp.Model.TweetSampleDataProvider;
import com.github.scribejava.core.model.OAuth1AccessToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class MainActivity extends Activity implements Observer {
    private ListView tweetList;
    private final String TAG = "mainactivity";
    public static ArrayList<Tweet> tweetslist =TweetSampleDataProvider.tweetsTimeline ;
    private TweetListAdapter tweetListAdapter;
    private Activity activity;

    //imageView
    private ImageView mHomeBtn;
    private ImageView mSearchBtn ;
    private ImageView mMessageBtn ;
    private ImageView mAlertBtn ;
    private ImageView userImage;
    private ImageView mPost;
    private TwitterButtons twitterButtons;


    final OpenAuthentication authentication = OpenAuthentication.getInstance();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //setting up Timeline
        authentication.addObserver(this);
        tweetList = findViewById(R.id.tweetsList);
        userImage = findViewById(R.id.acUserimage);
        mSearchBtn = findViewById(R.id.searchBtn);
        twitterButtons = findViewById(R.id.hTwitterButtons);
        if(!authentication.isAuthorized()){
        authorisationIntent();}

        tweetListAdapter = new TweetListAdapter(this, R.layout.tweet, tweetslist);
        tweetList.setAdapter(tweetListAdapter);
        if(TweetSampleDataProvider.tweetsTimeline!=null){
            tweetListAdapter.notifyDataSetChanged();
            tweetList.invalidate();
        }
//        tweetList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Intent detailedTweet = new Intent(MainActivity.this,DetailedTweet.class);
//                detailedTweet.putExtra(INDEX,i);
//                detailedTweet.putExtra(TAG,"MainActivity");
//                startActivity(detailedTweet);
//            }
//        });

        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //add intent to access user profile
                Intent intent = new Intent(MainActivity.this,Userprofile.class);
                startActivity(intent);
            }
        });
        mPost = findViewById(R.id.mPost);
        mPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText edittext = new EditText(getBaseContext());
                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Post a Tweet.")
                        .setMessage("Please enter up to 140 characters.")
                        .setView(edittext)
                        .setPositiveButton("Post", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Editable YouEditTextValue = edittext.getText();
                                while(YouEditTextValue.length()>140){
                                    Toast.makeText(MainActivity.this,"Character limit passed",Toast.LENGTH_SHORT).show();
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
                Intent searchIntent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(searchIntent);
            }
        });
    }


    public void authorisationIntent() {
        Intent authIntent = new Intent(MainActivity.this, AuthenticationWebView.class);
        startActivity(authIntent);
    }

    @Override
    public void update(Observable observable, Object o) {
        tweetListAdapter.notifyDataSetChanged();
        tweetList.invalidate();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (authentication.isAuthorized()) {
            authentication.addObserver(this);
            authentication.callTweetTask();
            authentication.callProfileTimeline();
            authentication.callMentionsTimeline();
            tweetListAdapter.notifyDataSetChanged();
            tweetList.invalidate();
            authentication.setUser();
            if(TweetSampleDataProvider.currentUser!=null) {
                Picasso.get().load(TweetSampleDataProvider.currentUser.getProfile_image_url()).transform(new TweetListAdapter.CircleTransform()).into(userImage);
                userImage.invalidate();
                tweetListAdapter.notifyDataSetChanged();
                tweetList.invalidate();
                authentication.callMentionsTimeline();
            }
        }else{
            authorisationIntent();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        tweetListAdapter.notifyDataSetChanged();
        tweetList.invalidate();
    }
}



