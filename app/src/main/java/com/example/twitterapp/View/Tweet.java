package com.example.twitterapp.View;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.twitterapp.R;

/**
 * Created by Kyle on 10-Jun-18.
 */

public class Tweet extends View {
    private ImageView favoritebtn = findViewById(R.id.favoritebtn);
    private ImageView replybtn = findViewById(R.id.replybtn);
    private TextView tweetName = findViewById(R.id.tweetName);
    private TextView tweetUsername = findViewById(R.id.tweetUsername);
    private TextView tweet = findViewById(R.id.tweet);
    private ImageView retweetbtn = findViewById(R.id.retweetbtn);
    private ImageView twitterlogo= findViewById(R.id.twitterlogo);

    public Tweet(Context context) {
        super(context);
    }

    public Tweet(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Tweet(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void setTweetName(String text) {
      // tweetName.setText(text);
    }

    public void setTweetUsername(String text) {
        //tweetUsername.setText(text);
    }

    public void setTweet(String text) {
      //tweet.setText(text);
    }
}
