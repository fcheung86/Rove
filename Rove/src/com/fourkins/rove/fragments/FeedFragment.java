package com.fourkins.rove.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fourkins.rove.R;

public class FeedFragment extends Fragment {

    public FeedFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.activity_feed, container, false);

        TextView t = (TextView) root.findViewById(R.id.textView1);
        t.setText("this is the feed view?");

        return root;
    }
}
