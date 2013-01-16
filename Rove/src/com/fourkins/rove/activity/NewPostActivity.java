package com.fourkins.rove.activity;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Locale;

import org.apache.http.entity.StringEntity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.fourkins.rove.R;
import com.fourkins.rove.application.AppPreferences;
import com.fourkins.rove.application.Rove;
import com.fourkins.rove.fragments.MapFragment;
import com.fourkins.rove.posts.Post;
import com.fourkins.rove.posts.PostsManager;
import com.fourkins.rove.util.LocationUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * Screen to submit new post. Can be initiated by clicking on "Add" menu button, or initiated from the map (ie. press
 * and hold location on the map). If triggered from "Add" button, location is populated with "Current location" (ie.
 * GPS)
 */
public class NewPostActivity extends Activity {

    private PostsManager mPostsManager;
    private AppPreferences mAppPrefs;
    private String mUserName;

    double latitudefromMap = 0;
    double longitudefromMap = 0;
    int fromMap = 0;

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

        mPostsManager = new PostsManager(this);

        // Automatically populate "Username" field with current logged in user
        mAppPrefs = new AppPreferences(getApplicationContext());
        mUserName = mAppPrefs.getUser();

        final TextView userText2 = (TextView) findViewById(R.id.edit_user);
        userText2.setText(mUserName);

        // hide the action bar
        ActionBar actionBar = getActionBar();
        actionBar.hide();
    }

    public void submitMessage(View view) {
        // final EditText userText = (EditText) findViewById(R.id.edit_user);
        final EditText latitudeText = (EditText) findViewById(R.id.edit_latitude);
        final EditText longitudeText = (EditText) findViewById(R.id.edit_longitude);
        final EditText messageText = (EditText) findViewById(R.id.edit_message);

        double latitude = 0;
        double longitude = 0;
        Address submitAddress = null;
        
        // If new post initiated from Map (ie. by holding location on the map)
        if (fromMap == 1) {
            latitude = latitudefromMap;
            longitude = longitudefromMap;
        } else { // If new post initiated from "Add" menu option
            latitude = Double.parseDouble(latitudeText.getText().toString());
            longitude = Double.parseDouble(longitudeText.getText().toString());
        }

        // String user = (String) userText.getText().toString();
        String message = messageText.getText().toString();

        Geocoder geoCoder = new Geocoder(this.getApplicationContext(), Locale.getDefault());
        try {
            List<Address> addresses = geoCoder.getFromLocation(latitude, longitude, 1);
            
            if (addresses.size() > 0) {
                submitAddress = addresses.get(0);
            }
        } catch (IOException e) {

        }

        Post post = new Post(mUserName, latitude, longitude, message, submitAddress.getAddressLine(0), submitAddress.getAddressLine(1), new Timestamp(System.currentTimeMillis()));
        mPostsManager.insertPost(post);

        finish();
    }

    public void submitMessageToServer(View view) {
        final EditText latitudeText = (EditText) findViewById(R.id.edit_latitude);
        final EditText longitudeText = (EditText) findViewById(R.id.edit_longitude);
        final EditText messageText = (EditText) findViewById(R.id.edit_message);

        double latitude = 0;
        double longitude = 0;
        
        Address submitAddress = null;

        if (fromMap == 1) {
            // If new post initiated from Map (ie. by holding location on the map)
            latitude = latitudefromMap;
            longitude = longitudefromMap;
        } else {
            // If new post initiated from "Add" menu option
            latitude = Double.parseDouble(latitudeText.getText().toString());
            longitude = Double.parseDouble(longitudeText.getText().toString());
        }

        String message = messageText.getText().toString();
        
        Geocoder geoCoder = new Geocoder(this.getBaseContext(), Locale.getDefault());
        try {
            List<Address> addresses = geoCoder.getFromLocation(latitude, longitude, 1);
            
            if (addresses.size() > 0) {
                submitAddress = addresses.get(0);
            }
        } catch (IOException e) {

        }
        
        Post post = new Post(mUserName, latitude, longitude, message, submitAddress.getAddressLine(0), submitAddress.getAddressLine(1), new Timestamp(System.currentTimeMillis()));

        try {
            // creates the Entity used for POST, converting the Post object into JSON
            // and getting the string value of the JSON
            StringEntity entity = new StringEntity(post.getJson().toString());

            AsyncHttpClient client = new AsyncHttpClient();

            // POSTs to the specified URL with the entity, with text/plain as the content type
            client.post(this, Rove.SERVER_BASE_URL + "/posts", entity, "text/plain", new AsyncHttpResponseHandler() {

                @Override
                public void onSuccess(String response) {
                    // this is the async callback, for now we aren't doing anything with it
                    // going forward, we can use this to know that the post is updated and add
                    // it to the feed list
                    System.out.println(response);
                }

            });
        } catch (UnsupportedEncodingException e) {

        }

        finish();
    }

    public void getGpsLocation(View view) {
        Location currentLocation = LocationUtil.getInstance(this).getCurrentLocation();

        if (currentLocation != null) {
            EditText latitude = (EditText) findViewById(R.id.edit_latitude);
            EditText longitude = (EditText) findViewById(R.id.edit_longitude);
            latitude.setText(Double.toString(currentLocation.getLatitude()));
            longitude.setText(Double.toString(currentLocation.getLongitude()));
        }
    }
}
