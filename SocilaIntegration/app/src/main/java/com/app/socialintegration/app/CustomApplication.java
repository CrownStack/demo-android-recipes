package com.app.socialintegration.app;

import android.app.Application;
import android.util.Log;

import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;

public class CustomApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        TwitterConfig config = new TwitterConfig.Builder(this)
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig("mr2cO9Nz2xN9zWF9TFPiYrbjz", "DXDFcyseN2L9GxjMcY67LbDVIM1qq5gtrCh7oXSGCG8l4ofBUm"))
                .debug(true)
                .build();
        Twitter.initialize(config);
    }
}
