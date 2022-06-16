package com.spotify.oauth2.api;

import com.spotify.oauth2.utils.ConfigLoader;
import io.restassured.response.Response;

import java.time.Instant;
import java.util.HashMap;

import static com.spotify.oauth2.api.RestResource.postAccount;

public class TokenManager {

    private static String access_token;
    private static Instant expiry_time;

    //using 'synchronized' here because to support multi-threading,
    //this ensures that given a multi-threading scenario, the getToken() method will only be released to next thread
    //after finished using
    public synchronized static String getToken(){
        try {
            if(access_token == null || Instant.now().isAfter(expiry_time)){
                System.out.println("Renewing token");
                Response response = renewToken();
                access_token = response.path("access_token");
                int expiryDurationInSeconds = response.path("expires_in");
                expiry_time = Instant.now().plusSeconds(expiryDurationInSeconds - 300);
            } else {
                System.out.println("Token is good to use");
            }

        } catch (Exception e){
            throw new RuntimeException("Abort! Failed to get token");
        }

        return access_token;
    }

    private static Response renewToken(){
        HashMap<String, String> formParams = new HashMap<String, String>();
        formParams.put("grant_type", ConfigLoader.getInstance().getGrantType());
        formParams.put("refresh_token", ConfigLoader.getInstance().getRefreshToken());
        formParams.put("client_id", ConfigLoader.getInstance().getClientId());
        formParams.put("client_secret", ConfigLoader.getInstance().getClientSecret());

        Response response = postAccount(formParams);

        if(response.statusCode() != 200){
            throw new RuntimeException("ABORT!!! Renew token failed");
        }
        return response;
    }
}
