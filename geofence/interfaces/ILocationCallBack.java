package geofenceex.com.mylibrary.interfaces;

import android.location.Location;

import geofenceex.com.mylibrary.utils.GeofenceManager;

public interface ILocationCallBack {
    void getFusedLocation(Location location);
    void getCurrentLocation(Location location);
    void createGeofence(GeofenceManager geofenceManager);
    void fusedLocationNotFound();
}
