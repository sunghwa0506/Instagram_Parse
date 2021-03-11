package com.example.instagram_parse;

import com.parse.Parse;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@ParseClassName("Post")
public class Post extends ParseObject {

    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_USER = "user";
    public static final String KEY_likes = "likes";

    public String getKeyDescription()
    {
        return getString(KEY_DESCRIPTION);
    }

    public void setKeyDescription(String description)
    {
        put(KEY_DESCRIPTION,description);
    }

    public ParseFile getImage(){
        return getParseFile(KEY_IMAGE);
    }

    public void setKeyImage(ParseFile parseFile)
    {
        put(KEY_IMAGE,parseFile);
    }

    public ParseUser getUser()
    {
        return getParseUser(KEY_USER);
    }

    public void setKeyUser(ParseUser user)
    {
        put(KEY_USER,user);
    }


    public  ArrayList<String> getKEY_likes(){

        return (ArrayList<String>)get(KEY_likes);

    }

    public void setKEY_likes(ArrayList<String> likes) {
        put(KEY_likes,likes);
    }



}
