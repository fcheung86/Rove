package com.fourkins.rove.fragments;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fourkins.rove.sqlite.posts.Post;
import com.fourkins.rove.sqlite.posts.PostsManager;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapFragment extends SupportMapFragment {

    private GoogleMap mMap;
    private PostsManager mPostsManager;
    private LatLngBounds bounds;
    private List<Post> posts;

    public MapFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle bundle) {
        View view = super.onCreateView(inflater, viewGroup, bundle);

        setUpMapIfNeeded();

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mPostsManager = new PostsManager(activity);
    }

    private void setUpMapIfNeeded() {
        if (mMap == null) {
            mMap = getMap();
            mMap.setOnCameraChangeListener(getCameraChangeListener());
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    public OnCameraChangeListener getCameraChangeListener() {
        return new OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition position) {
                bounds = mMap.getProjection().getVisibleRegion().latLngBounds;
                addMarkers(bounds);
            }
        };
    }

    private void setUpMap() {
        posts = mPostsManager.getAllPosts();
    }

    public void addMarkers(LatLngBounds latLngBounds) {
        
        mMap.clear();
        
        for (Post item : posts) {
            
            if (latLngBounds.southwest.latitude < item.getLatitude() && latLngBounds.southwest.longitude < item.getLongitude()) {
                if (latLngBounds.northeast.latitude > item.getLatitude() && latLngBounds.northeast.longitude > item.getLongitude()) {
                    mMap.addMarker(new MarkerOptions().position(new LatLng(item.getLatitude(), item.getLongitude())).title(item.getUserName())
                            .snippet(item.getMessage()));
                }
            }

        }
    }

}
