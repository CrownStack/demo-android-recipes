
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.android.inspectway.location.FusedLocation;
import com.android.inspectway.location.FusedLocationListener;

import butterknife.ButterKnife;

public class SampleActivity extends FragmentActivity implements FusedLocationListener {

    private final int REQUEST_PERMISSION_CODE = 200;
    private FusedLocation mFusedLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        getLocation();
    }

    private void getLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "enable permission", Toast.LENGTH_LONG).show();

            return;
        }
        getLatLong();
    }

    @RequiresPermission(allOf = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION})
    private void getLatLong() {
        mFusedLocation = new FusedLocation(this, this);
        mFusedLocation.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mFusedLocation != null) mFusedLocation.stopLocationUpdates();
    }

    @Override
    public void onLocationChanged(Location location) {
        // when the location is changed.
    }

    @Override
    public void onCheckedLocationSetting(boolean isEnabled) {
        if (isEnabled) mFusedLocation.startLocationUpdates();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {

                case FusedLocation.REQUEST_CHECK_SETTINGS:
                    switch (resultCode) {
                        case RESULT_OK:
                            mFusedLocation.startLocationUpdates();
                            break;
                    }
                    break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_PERMISSION_CODE: {
                if (grantResults.length > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                        getLocation();
                    }
                }
                break;
            }
        }
    }

}

