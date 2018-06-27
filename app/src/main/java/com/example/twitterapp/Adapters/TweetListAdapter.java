package com.example.twitterapp.Adapters;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.twitterapp.Model.Tweet;
import com.example.twitterapp.R;
import com.example.twitterapp.View.TweetView;
import com.squareup.picasso.Picasso;


import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Kyle on 08-Jun-18.
 */

public class TweetListAdapter extends ArrayAdapter{
    private LayoutInflater minflater;
    private List<Tweet> mTweets;



    public TweetListAdapter(@NonNull Context context, int resource, @NonNull List<Tweet> objects) {
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
            Tweet tweet = mTweets.get(position);
            TextView username = convertView.findViewById(R.id.tweetUsername);
            TextView name = convertView.findViewById(R.id.tweetName);
            TextView tweetText = convertView.findViewById(R.id.tweetText);
            TextView date = convertView.findViewById(R.id.tweetDate);
            ImageView profileImage = convertView.findViewById(R.id.tweetProfileImage);

            Picasso.get().load(tweet.getUser().getProfile_image_url()).into(profileImage);
            username.setText(tweet.getUser().getScreen_name());
            name.setText(tweet.getUser().getName());
            tweetText.setText(tweet.getText());
//            date.setText(String.valueOf(tweet.getCreated_at()));
        }
        return convertView;
    }
}
