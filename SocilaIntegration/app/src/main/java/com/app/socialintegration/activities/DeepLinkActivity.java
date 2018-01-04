package com.app.socialintegration.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.app.socialintegration.R;
import com.linkedin.platform.DeepLinkHelper;
import com.linkedin.platform.errors.LIDeepLinkError;
import com.linkedin.platform.listeners.DeepLinkListener;

public class DeepLinkActivity extends AppCompatActivity implements View.OnClickListener {

    DeepLinkHelper deepLinkHelper = DeepLinkHelper.getInstance();
    private TextView textViewDeeplinkError;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deeplink);

        textViewDeeplinkError = (TextView) findViewById(R.id.text_view_deeplink_error);
        Button buttonLiOpenProfile = (Button) findViewById(R.id.button_deeplink_open_profile);
        Button buttonLiMyProfile = (Button) findViewById(R.id.button_my_profile);
        buttonLiOpenProfile.setOnClickListener(this);
        buttonLiMyProfile.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        DeepLinkHelper deepLinkHelper = DeepLinkHelper.getInstance();
        deepLinkHelper.onActivityResult(this, requestCode, resultCode, data);
    }

    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.button_deeplink_open_profile:
                String profileId = ((EditText) findViewById(R.id.edit_text_deeplink_profile_id)).getText().toString();

                deepLinkHelper.openOtherProfile(DeepLinkActivity.this, profileId, new DeepLinkListener() {
                    @Override
                    public void onDeepLinkSuccess() {((TextView) findViewById(R.id.text_view_deeplink_error)).setText("");}

                    @Override
                    public void onDeepLinkError(LIDeepLinkError error) {textViewDeeplinkError.setText(error.toString());}
                });
                break;

            case R.id.button_my_profile:
                deepLinkHelper.openCurrentProfile(DeepLinkActivity.this, new DeepLinkListener() {
                    @Override
                    public void onDeepLinkSuccess() {textViewDeeplinkError.setText("");}

                    @Override
                    public void onDeepLinkError(LIDeepLinkError error) {textViewDeeplinkError.setText(error.toString());}
                });
                break;
        }
    }
}
