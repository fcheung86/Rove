package com.fourkins.rove.fragments;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.fourkins.rove.R;
import com.fourkins.rove.activity.NewPostActivity;
import com.fourkins.rove.posts.Post;
import com.fourkins.rove.posts.PostsManager;
import com.fourkins.rove.util.LocationUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * "Map" Tab (under Main Screen) Displays map (Google Map) with "pins" for each post Automatically zooms into the
 * current location
 * 
 */
public class MapFragment extends SupportMapFragment {

    public static final String INTENT_LAT = "com.fourkins.rove.LATITUDE";
    public static final String INTENT_LONG = "com.fourkins.rove.LONGITUDE";
    public static final String INTENT_FROM_MAP = "com.fourkins.rove.FROMMAP";

    private GoogleMap mMap;
    private PostsManager mPostsManager;
    private LatLngBounds mLatLngBounds;
    private List<Post> mPosts;
    private boolean mClearFlag = true;

    private LinearLayout mLinearLayout;
    private LinearLayout mMapLayout;

    public MapFragment() {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mPostsManager = new PostsManager(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle bundle) {
        View view = super.onCreateView(inflater, viewGroup, bundle);

        mLinearLayout = (LinearLayout) getActivity().findViewById(R.id.detailmapinfo);
        mMapLayout = (LinearLayout) getActivity().findViewById(R.id.emptyView);

        setUpMapIfNeeded();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPosts = mPostsManager.getAllPosts();
        addMarkers(mMap.getProjection().getVisibleRegion().latLngBounds);
    }

    private void setUpMapIfNeeded() {
        if (mMap == null) {
            mMap = getMap();
            mMap.setOnCameraChangeListener(getCameraChangeListener());
            mMap.setOnMarkerClickListener(getMarkerClickListener());
            mMap.setOnMapLongClickListener(getMapLongClickListener());
            mMap.setOnMapClickListener(getMapClickListener());
            mMap.setMyLocationEnabled(true);

            Location currentLocation = LocationUtil.getInstance(getActivity()).getCurrentLocation();
            if (currentLocation != null) {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), 12));
            }

            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                mPosts = mPostsManager.getAllPosts();
            }
        }
    }

    // Show markers on the map as user pans through the map
    public OnCameraChangeListener getCameraChangeListener() {
        return new OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition position) {
                mLatLngBounds = mMap.getProjection().getVisibleRegion().latLngBounds;
                addMarkers(mLatLngBounds);
            }
        };
    }

    public OnMarkerClickListener getMarkerClickListener() {
        return new OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1.25f);
                LayoutParams mapParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 0.75f);

                final TextView user = (TextView) mLinearLayout.findViewById(R.id.detail_map_user_display);
                final TextView latitudeDisplay = (TextView) mLinearLayout.findViewById(R.id.detail_map_latitude_display);
                final TextView longitudeDisplay = (TextView) mLinearLayout.findViewById(R.id.detail_map_longitude_display);
                final TextView comment = (TextView) mLinearLayout.findViewById(R.id.detail_map_message_display);

                user.setText(marker.getTitle());
                latitudeDisplay.setText(Double.toString(marker.getPosition().latitude));
                longitudeDisplay.setText(Double.toString(marker.getPosition().longitude));
                comment.setText(marker.getSnippet());

                mMapLayout.setLayoutParams(mapParams);
                mLinearLayout.setLayoutParams(params);

                if (!marker.getTitle().isEmpty()) {
                    mClearFlag = false;
                    return false;
                } else {
                    return true;
                }
            }
        };
    }

    public OnMapLongClickListener getMapLongClickListener() {
        return new OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latlng) {
                Intent intent = new Intent(getActivity(), NewPostActivity.class);
                intent.putExtra(INTENT_LAT, latlng.latitude);
                intent.putExtra(INTENT_LONG, latlng.longitude);
                intent.putExtra(INTENT_FROM_MAP, 1);
                startActivity(intent);
            }
        };
    }

    public OnMapClickListener getMapClickListener() {
        return new OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latlng) {
                LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 2.0f);
                LayoutParams mapParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 0.0f);

                mMapLayout.setLayoutParams(mapParams);
                mLinearLayout.setLayoutParams(params);
            }
        };
    }

    public void addMarkers(LatLngBounds latLngBounds) {
        if (mClearFlag == true) {
            mMap.clear();
        }

        for (Post item : mPosts) {

            if (latLngBounds.southwest.latitude < item.getLatitude() && latLngBounds.southwest.longitude < item.getLongitude()) {
                if (latLngBounds.northeast.latitude > item.getLatitude() && latLngBounds.northeast.longitude > item.getLongitude()) {
                    mMap.addMarker(new MarkerOptions().position(new LatLng(item.getLatitude(), item.getLongitude())).title(item.getUsername())
                            .snippet(item.getMessage()));
                }
            }
        }

        mClearFlag = true;
    }

    public void loadFromLocal() {
        mPosts = mPostsManager.getAllPosts();
        addMarkers(mMap.getProjection().getVisibleRegion().latLngBounds);
    }

    public void loadFromServer() {
        mPostsManager.getPostsFromServer(new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(String response) {
                mPosts = mPostsManager.convertJsonToPosts(response);
                addMarkers(mMap.getProjection().getVisibleRegion().latLngBounds);
            }

        });
    }
}
