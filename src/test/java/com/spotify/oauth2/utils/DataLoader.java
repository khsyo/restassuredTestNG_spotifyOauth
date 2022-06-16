package com.spotify.oauth2.utils;

import java.util.Properties;

// this is a singleton class, meaning only will be initialized once
public class DataLoader {
    private final Properties properties;
    private static DataLoader dataLoader;

    // private constructor, as won't be used outside this class
    private DataLoader(){
        properties = PropertyUtils.propertyLoader("src/test/resources/data.properties");
    }

    public static DataLoader getInstance(){
        if(dataLoader == null){
            dataLoader = new DataLoader();
        }
        return dataLoader;
    }

    public String getPlaylistIdGet(){
        String prop = properties.getProperty("get_playlist_id");
        if(prop != null) return prop;
        else throw new RuntimeException("Property get_playlist_id is not specified in the data.properties file");
    }

    public String getPlaylistIdUpdate(){
        String prop = properties.getProperty("update_playlist_id");
        if(prop != null) return prop;
        else throw new RuntimeException("Property update_playlist_id is not specified in the data.properties file");
    }
}
