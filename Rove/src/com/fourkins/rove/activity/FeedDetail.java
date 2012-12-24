package com.fourkins.rove.activity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;

import com.fourkins.rove.R;
import com.fourkins.rove.fragments.FeedFragment;
import com.fourkins.rove.sqlite.posts.Post;
import com.fourkins.rove.sqlite.posts.PostsManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class FeedDetail extends FragmentActivity {
    
    private GoogleMap mMap = null;
    private UiSettings mUiSettings;
    private PostsManager mPostsManager;
    private Post post = null;
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_page);
        
        Intent intent = getIntent();
        
        mPostsManager = new PostsManager(this);
        
        long postId = intent.getLongExtra(FeedFragment.postIdValue, 0);
        post = mPostsManager.getPost(postId);
        
        // hide the action bar
        ActionBar actionBar = getActionBar();
        actionBar.hide();
        
        setUpMapIfNeeded();
    }
    
    private void setUpMapIfNeeded() {
        
        
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.detailmap)).getMap();
            mUiSettings = mMap.getUiSettings();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(post.getLatitude(), post.getLongitude()), 14));
                mMap.addMarker(new MarkerOptions().position(new LatLng(post.getLatitude(), post.getLongitude())));
                mUiSettings.setTiltGesturesEnabled(false);
                mUiSettings.setScrollGesturesEnabled(false);
                mUiSettings.setRotateGesturesEnabled(false);
                
                final TextView user = (TextView) findViewById(R.id.detail_user_display);
                final TextView latitudeDisplay = (TextView) findViewById(R.id.detail_latitude_display);
                final TextView longitudeDisplay = (TextView) findViewById(R.id.detail_longitude_display);
                final TextView comment = (TextView) findViewById(R.id.detail_message_display);
                user.setText(post.getUserName());
                latitudeDisplay.setText(Double.toString(post.getLatitude()));
                longitudeDisplay.setText(Double.toString(post.getLongitude()));
                comment.setText(post.getMessage());
            }
        }
    }
    
}
