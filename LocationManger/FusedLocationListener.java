package com.eqdepot.fusedLocation;

import android.location.Location;

public interface FusedLocationListener {

    void onLocationChanged(Location location);
    void onCheckedLocationSetting(boolean isEnabled);
}
