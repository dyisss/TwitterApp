package com.example.twitterapp.View;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
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
    private ImageView reply;
    private ImageView retweet;
    private ImageView like;


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
        reply = findViewById(R.id.replybtn);
        retweet = findViewById(R.id.retweetbtn);
        retweet.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                retweet.setImageResource(R.drawable.retweet);
                retweet.invalidate();
            }
        });
        like = findViewById(R.id.likebtn);
        like.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                like.setImageResource(R.drawable.like2);
                like.invalidate();
            }
        });

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
