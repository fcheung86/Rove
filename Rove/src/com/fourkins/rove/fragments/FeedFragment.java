package com.fourkins.rove.fragments;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fourkins.rove.R;
import com.fourkins.rove.activity.PostDetailActivity;
import com.fourkins.rove.sqlite.PostsSQLiteHelper;
import com.fourkins.rove.sqlite.posts.PostsManager;

/**
 * "Feed" Tab (under Main Screen) Displays list of posts for a given area.
 * 
 */
public class FeedFragment extends ListFragment {

    private PostsManager mPostsManager;
    public static final String postIdValue = "com.example.POSTIDVALUE";

    public FeedFragment() {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mPostsManager = new PostsManager(activity);
    }

    @Override
    public void onResume() {
        super.onResume();

        Cursor postCursor = mPostsManager.getAllPostsCursor();

        String[] from = new String[] { PostsSQLiteHelper.COLUMN_ID, PostsSQLiteHelper.COLUMN_MESSAGE, PostsSQLiteHelper.COLUMN_LATITUDE,
                PostsSQLiteHelper.COLUMN_USER_NAME, PostsSQLiteHelper.COLUMN_LONGITUDE };
        int[] to = new int[] { R.id.row_id, R.id.row_message, R.id.row_latitude, R.id.row_username, R.id.row_longitude };

        ListAdapter adapter = new SimpleCursorAdapter(getActivity(), R.layout.list_feed_row, postCursor, from, to, 0);

        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        TextView idView = (TextView) v.findViewById(R.id.row_id);

        long postId;

        try {
            postId = Long.parseLong(idView.getText().toString());

            Intent intent = new Intent(getActivity(), PostDetailActivity.class);
            intent.putExtra(postIdValue, postId);
            startActivity(intent);

        } catch (NumberFormatException e) {
            Toast.makeText(getActivity(), "Invalid Post", Toast.LENGTH_SHORT).show();
        }
    }
}
