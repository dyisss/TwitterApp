package com.example.twitterapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    private ListView tweetList;
    private TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text = findViewById(R.id.text);

        tweetList = findViewById(R.id.tweetList);
        InputStream is = getBaseContext().getResources().openRawResource(R.raw.tweets);
        try{
            byte [] b = new byte[is.available()];
            is.read(b);
            String fileContent = new String(b);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}



