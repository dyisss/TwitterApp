package com.example.twitterapp.View;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.twitterapp.R;

/**
 * Created by Kyle on 10-Jun-18.
 */

public class TweetView extends RelativeLayout {
    private TextView tweetName ;
    private TextView tweetUsername ;
    private TextView tweet;


    public TweetView(Context context) {
        super(context);
        init();
    }

    public TweetView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TweetView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    public void init() {
       LayoutInflater inflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            inflater.inflate(R.layout.tweet,this);
        }
        tweetName = findViewById(R.id.tweetName);
        tweetUsername = findViewById(R.id.tweetUsername);
        tweet  = findViewById(R.id.tweetText);
    }


    public void setTweet(String text) {
        tweet.setText(text);
    }

    public void setTweetName(String text){
        tweetName.setText(text);
    }

    public void setTweetUsername(String text){
        tweetUsername.setText(text);
    }
}
