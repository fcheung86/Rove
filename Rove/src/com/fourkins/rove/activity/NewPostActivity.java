package com.fourkins.rove.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.fourkins.rove.R;
import com.fourkins.rove.fragments.MapFragment;
import com.fourkins.rove.sqlite.posts.Post;
import com.fourkins.rove.sqlite.posts.PostsManager;

public class NewPostActivity extends Activity {

    private PostsManager mPostsManager;

    double latitudefromMap = 0;
    double longitudefromMap = 0;
    int fromMap = 0;

    private LocationManager mLocationManager;
    public static boolean gpsEnabled;
    // private static final int TEN_SECONDS = 10000;
    // private static final int TEN_METERS = 10;
    private static final int TWO_MINUTES = 1000 * 60 * 2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        Intent intent = getIntent();
        if (intent.getIntExtra(MapFragment.fromMap, 0) == 1) {
            fromMap = intent.getIntExtra(MapFragment.fromMap, 0);
            latitudefromMap = intent.getDoubleExtra(MapFragment.intentLat, 0);
            longitudefromMap = intent.getDoubleExtra(MapFragment.intentLong, 0);

            final EditText latitudeText = (EditText) findViewById(R.id.edit_latitude);
            final EditText longitudeText = (EditText) findViewById(R.id.edit_longitude);
            latitudeText.setText(String.valueOf(latitudefromMap));
            longitudeText.setText(String.valueOf(longitudefromMap));
        }
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        gpsEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        mPostsManager = new PostsManager(this);

        // hide the action bar
        ActionBar actionBar = getActionBar();
        actionBar.hide();
    }

    public void submitMessage(View view) {
        final EditText userText = (EditText) findViewById(R.id.edit_user);
        final EditText latitudeText = (EditText) findViewById(R.id.edit_latitude);
        final EditText longitudeText = (EditText) findViewById(R.id.edit_longitude);
        final EditText messageText = (EditText) findViewById(R.id.edit_message);

        double latitude = 0;
        double longitude = 0;

        if (fromMap == 1) {
            latitude = latitudefromMap;
            longitude = longitudefromMap;
        } else {
            latitude = Double.parseDouble(latitudeText.getText().toString());
            longitude = Double.parseDouble(longitudeText.getText().toString());
        }

        String user = (String) userText.getText().toString();
        String message = (String) messageText.getText().toString();

        Post post = new Post(user, latitude, longitude, message);
        mPostsManager.insertPost(post);

        finish();
    }

    public void getGpsLocation(View view) {
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

        if (currentLocation != null) {
            EditText latitude = (EditText) findViewById(R.id.edit_latitude);
            EditText longitude = (EditText) findViewById(R.id.edit_longitude);
            latitude.setText(Double.toString(currentLocation.getLatitude()));
            longitude.setText(Double.toString(currentLocation.getLongitude()));
        }
    }

    private Location requestUpdatesFromProvider(final String provider, final String errorMsg) {
        Location location = null;
        if (mLocationManager.isProviderEnabled(provider)) {
            // mLocationManager.requestLocationUpdates(provider, TEN_SECOND, TEN_METERS, listener);
            location = mLocationManager.getLastKnownLocation(provider);
        } else {
            Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show();
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
