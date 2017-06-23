package com.app.socialintegration.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.app.socialintegration.R;
import com.app.socialintegration.helper.Message;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

public class MainActivity extends AppCompatActivity {

    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        callbackManager = CallbackManager.Factory.create();
        LoginButton buttonLogin = (LoginButton) findViewById(R.id.button_facebook_login);
        buttonLogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Message.message(getApplicationContext(), "success");
            }

            @Override
            public void onCancel() {
                Message.message(getApplicationContext(), "cancel");
            }

            @Override
            public void onError(FacebookException error) {
                Message.message(getApplicationContext(), "failed");
            }
        });
    }
}