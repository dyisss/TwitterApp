package com.example.twitterapp.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.twitterapp.Model.Tweet;
import com.example.twitterapp.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

import static com.example.twitterapp.MainActivity.authentication;

/**
 * Created by Kyle on 30-Jun-18.
 */

public class ProfileTweetListAdapter extends ArrayAdapter<Tweet> {
    private LayoutInflater minflater;
    private List<Tweet> mTweets;


    public ProfileTweetListAdapter(@NonNull Context context, int resource, @NonNull List<Tweet> objects) {
        super(context, R.layout.tweet, objects);
        minflater = LayoutInflater.from(context);
        mTweets = objects;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    public View getView(int position , @Nullable View convertView , @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = minflater.inflate(R.layout.tweet, parent, false);
        } else {
            final Tweet tweet = mTweets.get(position);
            TextView username = convertView.findViewById(R.id.tweetUsername);
            TextView name = convertView.findViewById(R.id.tweetName);
            TextView tweetText = convertView.findViewById(R.id.tweetText);
            TextView date = convertView.findViewById(R.id.tweetDate);
            ImageView profileImage = convertView.findViewById(R.id.tweetProfileImage);
            final ImageView reply = convertView.findViewById(R.id.replybtn);
            final ImageView retweet = convertView.findViewById(R.id.retweetbtn);
            final ImageView like = convertView.findViewById(R.id.likebtn);
            retweet.setImageResource(R.drawable.retweet);
            retweet.invalidate();

            tweet.getUser().setProfile_image_url(tweet.getUser().getProfile_image_url());
            Picasso.get().load(tweet.getUser().getProfile_image_url()).transform(new TweetListAdapter.CircleTransform( )).into(profileImage);
            username.setText("@" +tweet.getUser().getScreen_name());
            name.setText(tweet.getUser().getName());
            tweetText.setText(tweet.getText());
            date.setText(tweet.getCreated_at());
            reply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
            retweet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    retweet.setImageResource(R.drawable.retweet2);
                    retweet.invalidate();
                    if (!tweet.isRetweeted()) {
                        authentication.retweet(tweet.getId());
                        tweet.setRetweeted(true);
                    }else{
                        authentication.unRetweet(tweet.getId());
                        retweet.setImageResource(R.drawable.retweet);
                        retweet.invalidate();
                        tweet.setRetweeted(false);
                    }
                }
            });
            like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!tweet.isLiked()) {
                        authentication.like(tweet.getId());
                        like.setImageResource(R.drawable.like);
                        like.invalidate();
                        tweet.setLiked(true);

                    } else {
                        authentication.unlike(tweet.getId());
                        like.setImageResource(R.drawable.like2);
                        like.invalidate();
                        tweet.setLiked(false);
                    }
                }
            });
        }
        return convertView;
    }
}
