package com.fourkins.rove.posts;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.fourkins.rove.R;

public class PostAdapter extends ArrayAdapter<Post> {

    private int layoutResourceId;
    List<Post> posts = null;

    public PostAdapter(Context context, int layoutResourceId, List<Post> posts) {
        super(context, layoutResourceId, posts);
        this.layoutResourceId = layoutResourceId;
        this.posts = posts;
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

            TextView rowLatitude = (TextView) view.findViewById(R.id.row_latitude);
            rowLatitude.setText(String.format("%.3f", post.getLatitude()));

            TextView rowLongitude = (TextView) view.findViewById(R.id.row_longitude);
            rowLongitude.setText(String.format("%.3f", post.getLongitude()));
        }

        return view;
    }

}
