package com.example.twitterapp.View;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.twitterapp.R;

/**
 * Created by Kyle on 10-Jun-18.
 */

public class Tweet extends ConstraintLayout {
    private ImageView favoritebtn;
    private ImageView replybtn;
    private TextView tweetName;
    private TextView tweetUsername;
    private TextView tweet;
    private ImageView retweetbtn;
    private ImageView twitterlogo;

    public Tweet(Context context) {
        super(context);
        init();
    }

    public Tweet(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Tweet(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init(){
        LayoutInflater inflater =(LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        inflater.inflate(R.layout.tweet,this);
        favoritebtn = findViewById(R.id.favoritebtn);
        replybtn = findViewById(R.id.replybtn);
        tweetName = findViewById(R.id.tweetName);
        tweetUsername = findViewById(R.id.tweetUsername);
        tweet = findViewById(R.id.tweet);
        retweetbtn = findViewById(R.id.retweetbtn);
        twitterlogo = findViewById(R.id.twitterlogo);
    }

    public void setFavoritebtn(ImageView favoritebtn) {
        this.favoritebtn = favoritebtn;
    }

    public void setReplybtn(ImageView replybtn) {
        this.replybtn = replybtn;
    }

    public void setTweetName(String text) {
        this.tweetName.setText(text);
    }

    public void setTweetUsername(String text) {
        this.tweetUsername.setText(text);
    }

    public void setTweet(String text) {
      this.tweet.setText(text);
    }

    public void setRetweetbtn(ImageView retweetbtn) {
        this.retweetbtn = retweetbtn;
    }

    public void setTwitterlogo(ImageView twitterlogo) {
        this.twitterlogo = twitterlogo;
    }
}
