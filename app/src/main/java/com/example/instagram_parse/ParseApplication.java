package com.example.instagram_parse;

import android.app.Application;
import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Post.class);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("byHDXtJmnXQs7tLP8AfaPNGcNoy6yUDvAiQznfff")
                .clientKey("yL78TJEgA0ErCFLoF64p9zDJc9YxnQxFq6mJE29v")
                .server("https://parseapi.back4app.com")
                .build()
        );

    }
}

