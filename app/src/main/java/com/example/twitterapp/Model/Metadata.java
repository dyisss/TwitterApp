package com.example.twitterapp.Model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Kyle on 08-Jun-18.
 */

class Metadata {
    private String iso_language_code;
    private String result_type;

    public Metadata(String iso_language_code,String result_type){
        this.iso_language_code = iso_language_code;
        this.result_type =result_type;
    }

    public Metadata(JSONObject object){
        try {
            this.iso_language_code = object.getString("iso_language_code");
            this.result_type = object.getString("result_type");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getIso_language_code() {
        return iso_language_code;
    }

    public String getResult_type() {
        return result_type;
    }
}
