package geofenceex.com.mylibrary.interfaces;

import android.location.Location;

import geofenceex.com.mylibrary.utils.GeofenceManager;

/**
 * Created by ankur on 1/10/18.
 */

public interface ILocationCallBack {
    void getFusedLocation(Location location);
    void getCurrentLocation(Location location);
    void createGeofence(GeofenceManager geofenceManager);
    void fusedLocationNotFound();
}
