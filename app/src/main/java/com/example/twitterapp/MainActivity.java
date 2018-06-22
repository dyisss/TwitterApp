package com.example.twitterapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.example.twitterapp.Adapters.TweetListAdapter;
import com.example.twitterapp.Model.MyAsyncTask;
import com.example.twitterapp.Model.OpenAuthentication;
import com.example.twitterapp.Model.SearchMetaData;
import com.example.twitterapp.Model.Status;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ListView tweetList;
    private TextView text;
    private String content;
    private TextView tvText;
    private final String TAG = "mainactivity";
    public static ArrayList<Status> statuses = new ArrayList<>();
    private SearchMetaData searchMetaData;
    private TweetListAdapter tweetListAdapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text = findViewById(R.id.tvText);
        MyAsyncTask asyncTask = new MyAsyncTask();
        asyncTask.execute();


//        text.setText(textOAuth);
//        tweetListAdapter = new TweetListAdapter(this,R.layout.tweet,statuses);
//        tweetList = findViewById(R.id.tweetList);
//        tweetList.setAdapter(tweetListAdapter);
//        InputStream is = getBaseContext().getResources().openRawResource(R.raw.tweets);
//        try{
//            byte [] b = new byte[is.available()];
//            is.read(b);
//            content = new String(b);
//        }catch (IOException e){
//            e.printStackTrace();
//        }
//
//        loadJSON();

    }

    public void loadJSON(){

        InputStream is = getBaseContext().getResources().openRawResource(R.raw.tweets);
        try{
            byte [] b = new byte[is.available()];
            is.read(b);
            content = new String(b);
        }catch (IOException e){
            e.printStackTrace();
        }

        try{
            JSONObject tweets = new JSONObject(content);

            JSONObject metadata = tweets.getJSONObject("search_metadata");
            JSONArray statuses = tweets.getJSONArray("statuses");
            searchMetaData = new SearchMetaData(metadata);

            for (int i = 0; i < statuses.length(); i++){
                this.statuses.add(new Status(statuses.getJSONObject(i)));
            }



        }catch(JSONException e){
            Log.d(TAG, "loading json");
        }
    }
}



