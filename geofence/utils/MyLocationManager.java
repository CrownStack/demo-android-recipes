package geofenceex.com.mylibrary.utils;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import geofenceex.com.mylibrary.interfaces.ILocationCallBack;

/**
 * Created by ankur on 1/10/18.
 */

public class MyLocationManager implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "MyLocationManager";
    private static MyLocationManager myLocationManager;
    private FusedLocationProviderClient mFusedLocationClient;
    private ILocationCallBack iLocationCallBack;
    private GoogleApiClient googleApiClient;
    private Activity mActivity;
    private Marker currentLocationMarker;
    private GeofenceManager geofenceManager;

    public static MyLocationManager getLocationManager() {
        if (myLocationManager == null)
            myLocationManager = new MyLocationManager();
        return myLocationManager;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (geofenceManager != null)
            iLocationCallBack.createGeofence(geofenceManager);
        getFusedLocation();
        startLocationMonitor();
    }

    @Override
    public void onConnectionSuspended(int i) { }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) { }

    private LocationRequest locationRequest() {
        return LocationRequest.create()
                .setInterval(2000)
                .setFastestInterval(1000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    public void init(ILocationCallBack locationCallBack, Activity activity, boolean geofenceRequired) {
        iLocationCallBack = locationCallBack;
        mActivity = activity;
        googleApiClient = new GoogleApiClient.Builder(activity)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
        googleApiClient.connect();

        if (geofenceRequired) {
            geofenceManager = GeofenceManager.getGeofenceManager();
            geofenceManager.init(mActivity);
        }
    }

    public void getFusedLocation() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(mActivity);
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(mActivity, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null)
                            iLocationCallBack.getFusedLocation(location);
                        else
                            iLocationCallBack.fusedLocationNotFound();
                    }
                });
    }

    public void addCircles(double radius, GoogleMap googleMap, LatLng... latLngs) {
        for (LatLng latLng : latLngs) {
            googleMap.addCircle(new CircleOptions()
                    .center(new LatLng(latLng.latitude, latLng.longitude))
                    .radius(radius)
                    .strokeColor(Color.GRAY)
                    .fillColor(Color.GRAY)
                    .strokeWidth(4f));
        }
    }

    public void updateMarker(Location location, GoogleMap map, String title, boolean isCurrentLocMarker) {
        if (currentLocationMarker != null) {
            currentLocationMarker.remove();
        }
        MarkerOptions markerOptions = new MarkerOptions();
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        markerOptions.position(latLng);
        markerOptions.title(title);
        if (isCurrentLocMarker)
            currentLocationMarker = map.addMarker(markerOptions);
        else
            map.addMarker(markerOptions);
        map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        map.setMinZoomPreference(11f);
    }

    public void onDestroy(PendingIntent pendingIntent) {
        geofenceManager.onDestroy(pendingIntent);
        mFusedLocationClient = null;
        iLocationCallBack = null;
        mActivity = null;
    }

    private void startLocationMonitor() {
        try {
            mFusedLocationClient.requestLocationUpdates(locationRequest(), new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    Location location = locationResult.getLastLocation();
                    iLocationCallBack.getCurrentLocation(location);
                }
            }, Looper.getMainLooper());
        } catch (SecurityException e) {
            Log.d(TAG, e.getMessage());
        }
    }

    public boolean isLocationEnabled() {
        LocationManager lm = (LocationManager) mActivity.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled, network_enabled;

        if (lm == null) return false;
        gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        
        if (!gps_enabled && !network_enabled) return false;

        return true;
    }
}
