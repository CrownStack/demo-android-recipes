package geofenceex.com.mylibrary.utils;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;

public class Constants {
    public static final String GEOFENCE_ID = "STAN_UNI";
    public static final float GEOFENCE_RADIUS_IN_METERS = 30;
    public static final HashMap<String, LatLng> AREA_LANDMARKS = new HashMap<String, LatLng>();

    static {
        // stanford university.
        AREA_LANDMARKS.put(GEOFENCE_ID, new LatLng(37.427476, -122.170262));
    }
}
