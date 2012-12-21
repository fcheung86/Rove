package com.fourkins.rove.fragments;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;

import com.fourkins.rove.R;
import com.fourkins.rove.sqlite.PostsSQLiteHelper;
import com.fourkins.rove.sqlite.posts.PostsManager;

public class FeedFragment extends ListFragment {

    private PostsManager mPostsManager;

    public FeedFragment() {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mPostsManager = new PostsManager(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = super.onCreateView(inflater, container, savedInstanceState);

        Cursor postCursor = mPostsManager.getAllPostsCursor();

        String[] from = new String[] { PostsSQLiteHelper.COLUMN_MESSAGE, PostsSQLiteHelper.COLUMN_LATITUDE, PostsSQLiteHelper.COLUMN_USER_NAME,
                PostsSQLiteHelper.COLUMN_LONGITUDE };
        int[] to = new int[] { R.id.row_message, R.id.row_latitude, R.id.row_username, R.id.row_longitude };

        ListAdapter adapter = new SimpleCursorAdapter(getActivity(), R.layout.list_feed_row, postCursor, from, to, 0);

        setListAdapter(adapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        SimpleCursorAdapter adapter = (SimpleCursorAdapter) this.getListAdapter();
        adapter.notifyDataSetChanged();
    }
}
