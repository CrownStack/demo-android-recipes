package com.app.socialintegration.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.app.socialintegration.R;
import com.app.socialintegration.helper.Message;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

public class TwitterActivity extends AppCompatActivity {

    private TwitterLoginButton buttonTwitterLogin;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter);

        buttonTwitterLogin = (TwitterLoginButton) findViewById(R.id.button_twitter_login);
        buttonTwitterLogin.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();
                TwitterAuthToken authToken = session.getAuthToken();
                String token = authToken.token;
                String secret = authToken.secret;
                Message.message(getApplicationContext(), "success \n " + result.data + "\n" + token + "\n" + secret);
            }

            @Override
            public void failure(TwitterException exception) {
            Message.message(getApplicationContext(), exception.toString());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        buttonTwitterLogin.onActivityResult(requestCode, resultCode, data);
    }
}
