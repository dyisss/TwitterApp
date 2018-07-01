package com.example.twitterapp.View;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.twitterapp.Alerts;
import com.example.twitterapp.MainActivity;
import com.example.twitterapp.R;

/**
 * Created by Kyle on 30-Jun-18.
 */

public class TwitterButtons extends LinearLayout {
    //ImageViews
    public static ImageView homeBtn;
    public static ImageView searchBtn;
    public static ImageView messageBtn;
    public static ImageView alertBtn;

    public TwitterButtons(Context context) {
        super(context);
        init();
    }

    public TwitterButtons(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TwitterButtons(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init(){
        LayoutInflater inflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            inflater.inflate(R.layout.twitterbuttons,this);
        }
        homeBtn = this.findViewById(R.id.homeBtn);
        homeBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(getContext().getApplicationContext(),MainActivity.class);
                getContext().getApplicationContext().startActivity(intent);
            }
        });
        searchBtn = this.findViewById(R.id.searchBtn);
        messageBtn = this.findViewById(R.id.messageBtn);
        alertBtn = this.findViewById(R.id.alertBtn);
        alertBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext().getApplicationContext(),Alerts.class);
                getContext().getApplicationContext().startActivity(intent);
            }
        });
    }
}
