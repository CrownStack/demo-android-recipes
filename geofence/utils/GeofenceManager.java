package geofenceex.com.mylibrary.utils;

import android.app.Activity;
import android.app.PendingIntent;
import android.support.annotation.NonNull;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;

/**
 * Created by ankur on 1/10/18.
 */

public class GeofenceManager {
    private static GeofenceManager geofenceManager;
    private ArrayList<Geofence> mGeofenceList;
    private GeofencingClient mGeofencingClient;
    private Activity mContext;

    public static GeofenceManager getGeofenceManager() {
        if (geofenceManager == null)
            geofenceManager = new GeofenceManager();
        return geofenceManager;
    }

    public void init(Activity context) {
        mContext = context;
        mGeofenceList = new ArrayList<>();
        mGeofencingClient = LocationServices.getGeofencingClient(context);
    }

    public void createGeofence(float radius, PendingIntent pendingIntent, LatLng... latLngs) {
        for (LatLng latLng : latLngs) {
            mGeofenceList.add(new Geofence.Builder()
                    // Set the request ID of the geofence. This is a string to identify this
                    // geofence.
                    .setRequestId(Constants.GEOFENCE_ID)

                    .setCircularRegion(
                            latLng.latitude,
                            latLng.longitude,
                            radius
                    )
                    .setExpirationDuration(Geofence.NEVER_EXPIRE)
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                    .build());
        }
        addGeofence(pendingIntent);
    }

    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER | GeofencingRequest.INITIAL_TRIGGER_EXIT | GeofencingRequest.INITIAL_TRIGGER_DWELL);
        builder.addGeofences(mGeofenceList);
        return builder.build();
    }

    private void addGeofence(PendingIntent pendingIntent) {
        mGeofencingClient.addGeofences(getGeofencingRequest(), pendingIntent)
                .addOnSuccessListener(mContext, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                    }
                })
                .addOnFailureListener(mContext, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }

    private void removeGeofence(PendingIntent pendingIntent) {
        mGeofencingClient.removeGeofences(pendingIntent)
                .addOnSuccessListener(mContext, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                    }
                })
                .addOnFailureListener(mContext, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }

    public void onDestroy(PendingIntent pendingIntent) {
        removeGeofence(pendingIntent);
        mGeofenceList = null;
        mGeofencingClient = null;
    }
}
