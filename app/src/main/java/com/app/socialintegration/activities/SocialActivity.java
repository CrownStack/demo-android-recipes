package com.app.socialintegration.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.app.socialintegration.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.analytics.FirebaseAnalytics;

public class SocialActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social);

        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "000");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Prinsu");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        initView();
    }

    private void initView() {
        Button buttonFacebook = (Button) findViewById(R.id.button_facebook);
        Button buttonGooglePlus = (Button) findViewById(R.id.button_google_plus);
        Button buttonTwitter = (Button) findViewById(R.id.button_twitter);
        Button buttonLinkedIn = (Button) findViewById(R.id.button_linkedin);

        buttonFacebook.setOnClickListener(this);
        buttonGooglePlus.setOnClickListener(this);
        buttonTwitter.setOnClickListener(this);
        buttonLinkedIn.setOnClickListener(this);
    }

    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.button_facebook:
                startActivity(new Intent(this, MainActivity.class));
                break;

            case R.id.button_google_plus:
                startActivity(new Intent(this, GooglePlusActivity.class));
                break;

            case R.id.button_twitter:
                startActivity(new Intent(this, TwitterActivity.class));
                break;

            case R.id.button_linkedin:
                startActivity(new Intent(this, LinkedInActivity.class));
                break;
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {}
}
