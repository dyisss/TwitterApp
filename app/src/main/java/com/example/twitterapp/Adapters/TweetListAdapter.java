package com.example.twitterapp.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.twitterapp.R;

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
  public View getView(int position , @Nullable View convertView , @NonNull ViewGroup parent){
        if(convertView == null){
            convertView = minflater.inflate(R.layout.tweet,parent,false);
        }
        return convertView;
  }
}
