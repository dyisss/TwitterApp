package com.example.twitterapp.Model;

import android.util.Log;

import com.github.scribejava.apis.TwitterApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuth1RequestToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth10aService;

import java.io.IOException;

/**
 * Created by yang- on 13/06/2018.
 */

public class OpenAuthentication {
    final public static String TAG = "OpenAuthentication";

    public OpenAuthentication() {

    }

    public String getOAuth(){
        final OAuth10aService service = new ServiceBuilder("EnqIn4E2YDcSuONFOcrj1yNwL")
                .apiSecret("23cwrKzOTNPCcumdaNQ9x7GZTqaWceaFqCrqrQoUbmY14dCpvR")
                .build(TwitterApi.instance());
        try{
            final OAuth1RequestToken requestToken = service.getRequestToken();
            String authUrl = service.getAuthorizationUrl(requestToken);
            final OAuth1AccessToken accessToken = service.getAccessToken(requestToken, authUrl);
            final OAuthRequest request = new OAuthRequest(Verb.GET, "https://api.twitter.com/1.1/account/verify_credentials.json");
            service.signRequest(accessToken, request); // the access token from step 4
            final Response response = service.execute(request);
            return response.getBody();
        }catch (Exception e){
            Log.d(TAG, e.toString());

            return e.toString();
        }
    }
}
