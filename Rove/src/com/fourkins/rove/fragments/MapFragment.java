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
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapFragment extends SupportMapFragment {

    private GoogleMap mMap;
    private PostsManager mPostsManager;

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

            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        List<Post> posts = mPostsManager.getAllPosts();

        for (Post post : posts) {
            if (post.getLatitude() < 90 && post.getLatitude() < 180) {
                mMap.addMarker(new MarkerOptions().position(new LatLng(post.getLatitude(), post.getLongitude())).title(post.getUserName())
                        .snippet(post.getMessage() + " " + post.getLatitude() + "," + post.getLongitude()));
            }
        }
    }

}
