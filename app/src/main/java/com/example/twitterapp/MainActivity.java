package com.example.twitterapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    private ListView tweetList;
    private TextView text;
    private String content;
    private TextView tvText;

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
            content = new String(b);
        }catch (IOException e){
            e.printStackTrace();
        }

        tvText = findViewById(R.id.tvText);

        try{
            JSONObject tweets = new JSONObject(content);

            JSONObject metadata = tweets.getJSONObject("search_metadata");
            String refresh_url = metadata.getString("refresh_url");
            tvText.setText(refresh_url);

            JSONArray statuses = tweets.getJSONArray("statuses");

            for (int i = 0; i < statuses.length(); i++){
                JSONObject status = statuses.getJSONObject(i);
                String id_str = status.getString("id_str");

                tvText.setText(tvText.getText() + "\n" + id_str);
            }



        }catch(JSONException e){

        }
    }
}



