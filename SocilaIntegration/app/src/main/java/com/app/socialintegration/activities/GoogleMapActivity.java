package com.app.socialintegration.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.app.socialintegration.R;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

public class GoogleMapActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int PLACE_PICKER_REQUEST = 200;
    private TextView textViewGetPlace;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_map);

        textViewGetPlace = (TextView) findViewById(R.id.text_view_get_place);
        Button buttonPlacePicker = (Button) findViewById(R.id.button_place_picker);
        Button buttonPlaceAutoComplete = (Button) findViewById(R.id.button_place_auto_complete);
        Button buttonPlaceDetails = (Button) findViewById(R.id.button_place_details);

        buttonPlacePicker.setOnClickListener(this);
        buttonPlaceAutoComplete.setOnClickListener(this);
        buttonPlaceDetails.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.button_place_picker:
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    Intent intent = builder.build(GoogleMapActivity.this);
                    startActivityForResult(intent, PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
                break;

            case R.id.button_place_auto_complete:
                placeAutoCompleteMethod();
                break;
            case R.id.button_place_details:
                startActivity(new Intent(this, PlaceDetailsActivity.class));
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PLACE_PICKER_REQUEST) {
            if(resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this, data);
                String address = place.getAddress().toString();
                textViewGetPlace.setText(address);
            }
        }
    }

    protected void placeAutoCompleteMethod() {
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.fragment_place_autocomplete);
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                Log.e("Place", place.getName().toString());
            }

            @Override
            public void onError(Status status) {
                Log.e("Place", status.toString());
            }
        });
        autocompleteFragment.setHint("Search a Location");
    }
}
