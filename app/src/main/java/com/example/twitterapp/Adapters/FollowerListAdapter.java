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
import android.widget.ImageView;
import android.widget.TextView;

import com.example.twitterapp.Model.Tweet;
import com.example.twitterapp.Model.User;
import com.example.twitterapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kyle on 01-Jul-18.
 */

public class FollowerListAdapter extends ArrayAdapter<User> {
    private LayoutInflater minflater;
    private List<User> mUsers;



    public FollowerListAdapter(@NonNull Context context, int resource, @NonNull List<User> objects) {
        super(context, R.layout.user, objects);
        minflater = LayoutInflater.from(context);
        mUsers = objects;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    public View getView(int position , @Nullable View convertView , @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = minflater.inflate(R.layout.user, parent, false);
        } else {
            User user = mUsers.get(position);
            TextView screenName = convertView.findViewById(R.id.fScreenName);
            TextView name = convertView.findViewById(R.id.fUsername);
            TextView bio = convertView.findViewById(R.id.fBio);
            ImageView profileImage = convertView.findViewById(R.id.fUserImage);
            user.setProfile_image_url(user.getProfile_image_url());
            Picasso.get().load(user.getProfile_image_url()).transform(new TweetListAdapter.CircleTransform( )).into(profileImage);
            screenName.setText("@" +user.getScreen_name());
            name.setText(user.getName());
            bio.setText(user.getDescription());
        }
        return convertView;
    }
}

