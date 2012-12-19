package com.fourkins.rove.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fourkins.rove.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapFragment extends SupportMapFragment{

    private Context mContext;
    private GoogleMap mMap;
    //private MapView mMapView;

    public MapFragment() {

    }

    public MapFragment(Context context) {
        mContext = context;
    }
    
    /*public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }*/
    
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.activity_map, container, false);
        
        // initMap();
        setUpMapIfNeeded();
        return view;
    }
    
    private void setUpMapIfNeeded() {
    	if (mMap == null) {
    		mMap = ((SupportMapFragment)  getActivity().getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
    		
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
    	}
    }

    private void setUpMap() {
        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title(
                "Marker"));
    }
    
}
