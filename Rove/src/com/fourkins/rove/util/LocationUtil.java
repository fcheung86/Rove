package com.fourkins.rove.util;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

/**
 * Tool to detect current location through GPS or network provider
 * 
 */
public class LocationUtil {

    private static LocationUtil instance;

    private LocationManager mLocationManager;

    // private static final int TEN_SECONDS = 10000;
    // private static final int TEN_METERS = 10;
    private static final int TWO_MINUTES = 1000 * 60 * 2;

    private LocationUtil(Context context) {
        mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    public synchronized static LocationUtil getInstance(Context context) {
        if (instance == null) {
            instance = new LocationUtil(context);
        }

        return instance;
    }

    public Location getCurrentLocation() {
        Location gpsLocation = null;
        Location networkLocation = null;

        Location currentLocation = null;

        gpsLocation = requestUpdatesFromProvider(LocationManager.GPS_PROVIDER, "GPS NOT SUPPORTED");
        networkLocation = requestUpdatesFromProvider(LocationManager.NETWORK_PROVIDER, "NETWORK PROVIDER NOT SUPPORTED");

        // If both providers return last known locations, compare the two and use the better
        // one to update the UI. If only one provider returns a location, use it.
        if (gpsLocation != null && networkLocation != null) {
            currentLocation = getBetterLocation(gpsLocation, networkLocation);
        } else if (gpsLocation != null) {
            currentLocation = gpsLocation;
        } else if (networkLocation != null) {
            currentLocation = networkLocation;
        }

        return currentLocation;
    }

    private Location requestUpdatesFromProvider(final String provider, final String errorMsg) {
        Location location = null;
        if (mLocationManager.isProviderEnabled(provider)) {
            // mLocationManager.requestLocationUpdates(provider, TEN_SECOND, TEN_METERS, listener);
            location = mLocationManager.getLastKnownLocation(provider);
        }
        return location;
    }

    /**
     * Determines whether one Location reading is better than the current Location fix. Code taken from
     * http://developer.android.com/guide/topics/location/obtaining-user-location.html
     * 
     * @param newLocation
     *            The new Location that you want to evaluate
     * @param currentBestLocation
     *            The current Location fix, to which you want to compare the new one
     * @return The better Location object based on recency and accuracy.
     */
    protected Location getBetterLocation(Location newLocation, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return newLocation;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = newLocation.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved.
        if (isSignificantlyNewer) {
            return newLocation;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return currentBestLocation;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (newLocation.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(newLocation.getProvider(), currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return newLocation;
        } else if (isNewer && !isLessAccurate) {
            return newLocation;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return newLocation;
        }
        return currentBestLocation;
    }

    /** Checks whether two providers are the same */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }
}
