package com.fourkins.rove.posts;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.fourkins.rove.R;

public class PostAdapter extends ArrayAdapter<Post> {

    private int layoutResourceId;
    List<Post> posts = null;
    Location currentLoc = null;

    public PostAdapter(Context context, int layoutResourceId, List<Post> posts, Location currentLoc) {
        super(context, layoutResourceId, posts);
        this.layoutResourceId = layoutResourceId;
        this.posts = posts;
        this.currentLoc = currentLoc;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        // if the view is null, inflat it with the specified layout
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layoutResourceId, null);
        }

        Post post = posts.get(position);
        if (post != null) {
            // populate the different views in this layout
            TextView rowId = (TextView) view.findViewById(R.id.row_id);
            rowId.setText(String.valueOf(post.getPostId()));

            TextView rowMessage = (TextView) view.findViewById(R.id.row_message);
            rowMessage.setText(post.getMessage());

            TextView rowUsername = (TextView) view.findViewById(R.id.row_username);
            rowUsername.setText(post.getUsername());
            
            float[] resultArray = new float[99];
            Location.distanceBetween(currentLoc.getLatitude(), currentLoc.getLongitude(), post.getLatitude(), post.getLongitude(), resultArray);
            
            TextView rowdistance = (TextView) view.findViewById(R.id.row_distance);
            rowdistance.setText(" " + String.format("%.1f", resultArray[0]/1000) + " km");
            
            Geocoder geoCoder = new Geocoder(getContext(), Locale.getDefault());
            try{
                List<Address> addresses = geoCoder.getFromLocation(post.getLatitude(), post.getLongitude(), 1);
                
                if (addresses.size() > 0) {
                    TextView row_address = (TextView) view.findViewById(R.id.row_address);
                    row_address.setText(" " + addresses.get(0).getAddressLine(0) + " ," + addresses.get(0).getAddressLine(1));
                }
            }
            catch (IOException e){
                
            }
            //TextView rowLatitude = (TextView) view.findViewById(R.id.row_latitude);
            //rowLatitude.setText(String.format("%.3f", post.getLatitude()));

            //TextView rowLongitude = (TextView) view.findViewById(R.id.row_longitude);
            //rowLongitude.setText(String.format("%.3f", post.getLongitude()));
        }

        return view;
    }

}
