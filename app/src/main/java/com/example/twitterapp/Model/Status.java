package com.example.twitterapp.Model;

/**
 * Created by Kyle on 08-Jun-18.
 */

public class Status {
    private String created_at;
    private int id ;
    private int id_str;
    private String text;
    private Boolean trunacated;
    private Entity entity;
    private Metadata metadata;
    private String source;
    private int in_reply_to_status_id;
    private String in_reply_to_status_id_str;
    private String in_reply_to_screen_name;
    private User user;
    //private Geo;
    //private Coordinates;
    //private Contributors;
    private boolean is_quote_status;
    private int Retweet_count;
    private int Favorite_count;
    private Boolean Retweeted;
    private String Lang;

    public Status(String created_at , int id , int id_str , String text, Boolean trunacated ,Entity entity , Metadata metadata , String source , int in_reply_to_status_id ,String in_reply_to_status_id_str , String in_reply_to_screen_name ,User user,boolean is_quote_status,int Retweet_count , int Favorite_count,boolean Retweeted , String lang){
        this.created_at = created_at;
        this.id = id;
        this.id_str = id_str;
        this.text = text;
        this.trunacated = trunacated;
        this.entity = entity;
        this.metadata = metadata;
        this.source =source;
        this.in_reply_to_status_id=in_reply_to_status_id;
        this.in_reply_to_status_id_str = in_reply_to_status_id_str;
        this.in_reply_to_screen_name = in_reply_to_screen_name;
        this.user = user;
        this.is_quote_status = is_quote_status;
        this.Retweet_count = Retweet_count;
        this.Favorite_count = Favorite_count;
        this.Retweeted = Retweeted;
        this.Lang = lang;
    }

    public String getCreated_at() {
        return created_at;
    }

    public int getId() {
        return id;
    }

    public int getId_str() {
        return id_str;
    }

    public String getText() {
        return text;
    }

    public Boolean getTrunacated() {
        return trunacated;
    }

    public Entity getEntity() {
        return entity;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public String getSource() {
        return source;
    }

    public int getIn_reply_to_status_id() {
        return in_reply_to_status_id;
    }

    public String getIn_reply_to_status_id_str() {
        return in_reply_to_status_id_str;
    }

    public String getIn_reply_to_screen_name() {
        return in_reply_to_screen_name;
    }

    public User getUser() {
        return user;
    }

    public boolean isIs_quote_status() {
        return is_quote_status;
    }

    public int getRetweet_count() {
        return Retweet_count;
    }

    public int getFavorite_count() {
        return Favorite_count;
    }

    public Boolean getRetweeted() {
        return Retweeted;
    }

    public String getLang() {
        return Lang;
    }
}
