package com.nomadhub.nomadhub;

import com.parse.Parse;
import android.app.Application;

public class StarterApplication extends Application {

    // PARSE PWD hVB3gPJUoRl6

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("f3dabfc5f863362a40cd414e33a9d2d8fed9e671")
                // if defined
                .clientKey("709085cbf0aaaeda9a807812720e84d460ac3f44")
                .server("http://35.180.169.204:80/parse")
                .build()
        );
    }
}