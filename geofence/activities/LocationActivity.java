package geofenceex.com.mylibrary.activities;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import geofenceex.com.mylibrary.utils.Constants;
import geofenceex.com.mylibrary.services.GeofenceTransitionsIntentService;
import geofenceex.com.mylibrary.R;
import geofenceex.com.mylibrary.interfaces.IDialogCallback;
import geofenceex.com.mylibrary.interfaces.ILocationCallBack;
import geofenceex.com.mylibrary.dialoges.DialogManager;
import geofenceex.com.mylibrary.utils.GeofenceManager;
import geofenceex.com.mylibrary.utils.MyLocationManager;
import geofenceex.com.mylibrary.utils.PermissionManager;

public class LocationActivity extends PermissionManager implements OnMapReadyCallback, ILocationCallBack, IDialogCallback {

    private static final String TAG = LocationActivity.class.getName();
    private GoogleMap mMap;
    private PendingIntent mGeofencePendingIntent;
    private MyLocationManager myLocationManager;
    private DialogManager mDialogManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mDialogManager = DialogManager.getDialogManager();
        mDialogManager.init(this, this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        PermissionManager.checkPermission(this, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION);
    }

    @Override
    public void getFusedLocation(Location location) {
        myLocationManager.updateMarker(location, mMap, "Last Location", false);
    }

    @Override
    public void getCurrentLocation(Location location) {
        myLocationManager.updateMarker(location, mMap, "Current Location", true);
    }

    @Override
    public void createGeofence(GeofenceManager geofenceManager) {
        geofenceManager.createGeofence(Constants.GEOFENCE_RADIUS_IN_METERS,
                getGeofencePendingIntent(),
                new LatLng(28.4867280, 77.1049570),
                new LatLng(28.486611, 77.105265));
    }

    @Override
    public void fusedLocationNotFound() { }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (myLocationManager != null) {
            myLocationManager.onDestroy(getGeofencePendingIntent());
        }
    }

    @Override
    protected void permissionDenied() {
        //if required permission denied
        mDialogManager.showPermissionDialog("Permission", "Allow required Permission", "Ok", "Cancel",
                true, true, true, true);
    }

    @Override
    protected void permissionPermanentlyDenied() {
        mDialogManager.showSettingDialog("Permission", "Allow required Permission", "Ok", "Cancel");
    }

    @Override
    public void permissionAllowed() {
        if (myLocationManager == null) {
            myLocationManager = MyLocationManager.getLocationManager();
            myLocationManager.init(this, this, true);
        }
        if (mMap != null) {
            ArrayList<LatLng> latLngs = new ArrayList<>();
            latLngs.add(new LatLng(28.4867280, 77.1049570));
            latLngs.add(new LatLng(28.486611, 77.105265));
            myLocationManager.addCircles(Constants.GEOFENCE_RADIUS_IN_METERS,
                    mMap,
                    new LatLng(28.4867280, 77.1049570),
                    new LatLng(28.486611, 77.105265));
        }

        if (myLocationManager.isLocationEnabled())
            mDialogManager.gotoLocationSettings();
    }

    @Override
    public void dialogOkClick() {
        PermissionManager.checkPermission(this, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION);
    }

    @Override
    public void dialogSettingOkClick() {
        goToSettings();
    }

    @Override
    public void dialogCancelClick() {
        finish();
    }

    private PendingIntent getGeofencePendingIntent() {
        if (mGeofencePendingIntent != null) {
            return mGeofencePendingIntent;
        }
        Intent intent = new Intent(this, GeofenceTransitionsIntentService.class);
        mGeofencePendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.
                FLAG_UPDATE_CURRENT);
        return mGeofencePendingIntent;
    }
}
