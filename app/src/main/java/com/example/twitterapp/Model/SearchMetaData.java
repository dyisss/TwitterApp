package com.example.twitterapp.Model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by yang- on 08/06/2018.
 */

public class SearchMetaData {
    private int completed_in;
    private int max_id;
    private String max_id_str;
    private String next_results;
    private String query;
    private String refresh_url;
    private int count;
    private int since_id;
    private String since_id_str;

    public SearchMetaData(int completed_in, int max_id, String max_id_str, String next_results, String query, String refresh_url, int count, int since_id, String since_id_str) {
        this.completed_in = completed_in;
        this.max_id = max_id;
        this.max_id_str = max_id_str;
        this.next_results = next_results;
        this.query = query;
        this.refresh_url = refresh_url;
        this.count = count;
        this.since_id = since_id;
        this.since_id_str = since_id_str;
    }

    public SearchMetaData(JSONObject object){
        try{
            this.completed_in =object.getInt("completed_in");
            this.max_id = object.getInt("max_id");
            this.max_id_str = object.getString("max_id_str");
            this.next_results = object.getString("next_results");
            this.query = object.getString("query");
            this.refresh_url = object.getString("refresh_url");
            this.count = object.getInt("count");
            this.since_id = object.getInt("since_id");
            this.since_id_str = object.getString("since_id_str");
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public int getCompleted_in() {
        return completed_in;
    }

    public int getMax_id() {
        return max_id;
    }

    public String getMax_id_str() {
        return max_id_str;
    }

    public String getNext_results() {
        return next_results;
    }

    public String getQuery() {
        return query;
    }

    public String getRefresh_url() {
        return refresh_url;
    }

    public int getCount() {
        return count;
    }

    public int getSince_id() {
        return since_id;
    }

    public String getSince_id_str() {
        return since_id_str;
    }
}
