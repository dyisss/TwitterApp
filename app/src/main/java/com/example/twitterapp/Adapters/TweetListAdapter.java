package com.example.twitterapp.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.twitterapp.MainActivity;
import com.example.twitterapp.Model.Status;
import com.example.twitterapp.Model.User;
import com.example.twitterapp.R;
import com.example.twitterapp.View.Tweet;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kyle on 08-Jun-18.
 */

public class TweetListAdapter extends ArrayAdapter{
    private LayoutInflater minflater;



    public TweetListAdapter(@NonNull Context context, int resource, @NonNull List objects) {
        super(context, resource, objects);
        this.minflater = LayoutInflater.from(context);
    }

    @NonNull
  public View getView(int position , @Nullable View convertView , @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = minflater.inflate(R.layout.tweet, parent, false);
        } else {
            //Tweet tweetview = (Tweet) convertView.findViewById(R.id.tweetlayout);
            Status status = MainActivity.statuses.get(position);
            TextView textView = convertView.findViewById(R.id.tweet);
            textView.setText(status.getId_str());
            //tweetview.setTweet(status.getText());
            //tweetview.setTweetName(status.getUser().getName());
            //tweetview.setTweetUsername(status.getUser().getScreen_name());
        }
        return convertView;
    }
}
