package com.example.twitterapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

/**
 * Created by Kyle on 30-Jun-18.
 */

public class Alerts extends Activity {
    private ImageView aUserImage;
    private Button aAll;
    private Button aMention;
    private ListView aNotificationList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alert);
        aUserImage = findViewById(R.id.acUserimage);
        aAll = findViewById(R.id.aAll);
        aMention = findViewById(R.id.aMention);
        aNotificationList = findViewById(R.id.aNotifications);
    }
}
