package com.app.socialintegration.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.app.socialintegration.R;
import com.linkedin.platform.APIHelper;
import com.linkedin.platform.errors.LIApiError;
import com.linkedin.platform.listeners.ApiListener;
import com.linkedin.platform.listeners.ApiResponse;

import org.json.JSONObject;

public class ApiActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String HOST = "api.linkedin.com";
    private static final String TOP_CARD_URL = "https://" + HOST + "/v1/people/~:(first-name,last-name,public-profile-url)";
    private static final String SHARE_URL = "https://" + HOST + "/v1/people/~/shares";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api);

        final Button buttonMakeApiCall = (Button) findViewById(R.id.button_make_api_call);
        final Button buttonMakePostApiCall = (Button) findViewById(R.id.button_make_post_api_call);
        buttonMakeApiCall.setOnClickListener(this);
        buttonMakePostApiCall.setOnClickListener(this);
    }

    public void onClick(View v) {

        APIHelper apiHelper = APIHelper.getInstance(getApplicationContext());

        switch (v.getId()) {

            case R.id.button_make_api_call:

                apiHelper.getRequest(ApiActivity.this, TOP_CARD_URL, new ApiListener() {
                    @Override
                    public void onApiSuccess(ApiResponse apiResponse) {

                        JSONObject jsonObject = apiResponse.getResponseDataAsJson();

                       int StatusCode = apiResponse.getStatusCode();
                        String firstName = jsonObject.optString("firstName");
                        String lastName = jsonObject.optString("lastName");
                        String publicProfileUrl = jsonObject.optString("publicProfileUrl");

                        ((TextView) findViewById(R.id.text_view_response)).setText("StatusCode: " + StatusCode +
                                "\n" + "FirstName: " + firstName +
                                "\n" +"lastName: " + lastName +
                                "\n" + "publicProfileUrl: " + publicProfileUrl);
                    }

                    @Override
                    public void onApiError(LIApiError LIApiError) {
                        ((TextView) findViewById(R.id.text_view_response)).setText(LIApiError.toString());
                    }
                });
                break;

            case R.id.button_make_post_api_call:
                EditText shareComment = (EditText) findViewById(R.id.edit_text_share_comment);
                String shareJsonText = "{ \n" +
                        "   \"comment\":\"" + shareComment.getText() + "\"," +
                        "   \"visibility\":{ " +
                        "      \"code\":\"anyone\"" +
                        "   }," +
                        "   \"content\":{ " +
                        "      \"title\":\"Test Share Title\"," +
                        "      \"description\":\"Leverage the Share API to maximize engagement on user-generated content on LinkedIn\"," +
                        "      \"submitted-url\":\"https://www.americanexpress.com/us/small-business/openforum/programhub/managing-your-money/?pillar=critical-numbers\"," +
                        "      \"submitted-image-url\":\"http://m3.licdn.com/media/p/3/000/124/1a6/089a29a.png\"" +
                        "   }" +
                        "}";

                apiHelper.postRequest(ApiActivity.this, SHARE_URL, shareJsonText, new ApiListener() {
                    @Override
                    public void onApiSuccess(ApiResponse apiResponse) {

                        JSONObject jsonObject = apiResponse.getResponseDataAsJson();
                        int StatusCode = apiResponse.getStatusCode();
                        String updateKey = jsonObject.optString("updateKey");
                        String updateUrl = jsonObject.optString("updateUrl");
                        String location = apiResponse.getLocationHeader();
                        Log.v("response", StatusCode + " " + updateKey + " " + updateUrl + " " + location);

                        ((TextView) findViewById(R.id.text_view_response)).setText("StatusCode: " + StatusCode +
                        "\n" + "updateKey: " + updateKey +
                        "\n" + "updateUrl: " + updateUrl +
                        "\n" + "location: " + location);
                    }

                    @Override
                    public void onApiError(LIApiError LIApiError) {
                        ((TextView) findViewById(R.id.text_view_response)).setText(LIApiError.toString());}});
        }
    }
}
