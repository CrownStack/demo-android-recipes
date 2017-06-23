package com.app.socialintegration.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.app.socialintegration.R;
import com.app.socialintegration.helper.Message;
import com.linkedin.platform.LISession;
import com.linkedin.platform.LISessionManager;
import com.linkedin.platform.errors.LIAuthError;
import com.linkedin.platform.listeners.AuthListener;
import com.linkedin.platform.utils.Scope;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static android.content.pm.PackageManager.*;

public class LinkedInActivity extends AppCompatActivity implements View.OnClickListener {

    final private Activity thisActivity = this;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linkedin);

        Button buttonLiLogin = (Button) findViewById(R.id.button_login_li);
        Button buttonLiForget = (Button) findViewById(R.id.button_logout_li);
        Button buttonLiApiCall = (Button) findViewById(R.id.button_api_call);
        Button buttonLiDeepLink = (Button) findViewById(R.id.button_deeplink);
        Button buttonLiShowPckHash = (Button) findViewById(R.id.button_show_pck_hash);

        buttonLiLogin.setOnClickListener(this);
        buttonLiForget.setOnClickListener(this);
        buttonLiApiCall.setOnClickListener(this);
        buttonLiDeepLink.setOnClickListener(this);
        buttonLiShowPckHash.setOnClickListener(this);
    }

    private static Scope buildScope() {
        return Scope.build(Scope.R_BASICPROFILE, Scope.W_SHARE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        LISessionManager.getInstance(getApplicationContext()).onActivityResult(this, requestCode, resultCode, data);
    }

    private void setUpdateState() {
        LISessionManager sessionManager = LISessionManager.getInstance(getApplicationContext());
        LISession session = sessionManager.getSession();
        boolean accessTokenValid = session.isValid();

        ((TextView) findViewById(R.id.text_view_access_token)).setText(
                accessTokenValid ? session.getAccessToken().toString() : "Sync with LinkedIn to enable these buttons");
        Button buttonApiCall = ((Button) findViewById(R.id.button_api_call));
        Button buttonDeepLink = (Button) findViewById(R.id.button_deeplink);
        buttonApiCall.setEnabled(accessTokenValid);
        buttonDeepLink.setEnabled(accessTokenValid);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.button_login_li:
                LISessionManager.getInstance(getApplicationContext()).init(thisActivity, buildScope(), new AuthListener() {
                    @Override
                    public void onAuthSuccess() {
                        setUpdateState();
                        Message.message(getApplicationContext(), "success" + LISessionManager.getInstance(getApplicationContext())
                                .getSession().getAccessToken().toString());
                    }

                    @Override
                    public void onAuthError(LIAuthError error) {
                        setUpdateState();
                        ((TextView) findViewById(R.id.text_view_access_token)).setText(error.toString());
                        Message.message(getApplicationContext(), "failed " +error.toString());
                    }
                }, true);
                break;

            case R.id.button_logout_li:
                LISessionManager.getInstance(getApplicationContext()).clearSession();
                setUpdateState();
                break;

            case R.id.button_api_call:
                startActivity(new Intent(LinkedInActivity.this, ApiActivity.class));
                break;

            case R.id.button_deeplink:
                startActivity(new Intent(LinkedInActivity.this, DeepLinkActivity.class));
                break;

            case R.id.button_show_pck_hash:
                try {
                    PackageInfo info = getPackageManager().getPackageInfo(
                            getPackageName(),
                            GET_SIGNATURES);
                    for(Signature signature : info.signatures) {
                        try {
                            MessageDigest md = MessageDigest.getInstance("SHA");
                            md.update(signature.toByteArray());
                            ((TextView) findViewById(R.id.pckText)).setText(info.packageName);
                            ((TextView) findViewById(R.id.pckHashText)).setText(Base64.encodeToString(md.digest(), Base64.DEFAULT));
                            String str = Base64.encodeToString(md.digest(), Base64.DEFAULT);
                            Log.e("hask key", str);
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (NameNotFoundException e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
