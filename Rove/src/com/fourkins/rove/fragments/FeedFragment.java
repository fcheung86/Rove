package com.fourkins.rove.fragments;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.database.Cursor;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.fourkins.rove.R;
import com.fourkins.rove.application.Rove;
import com.fourkins.rove.sqlite.PostsSQLiteHelper;
import com.fourkins.rove.sqlite.posts.Post;
import com.fourkins.rove.sqlite.posts.PostsManager;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * "Feed" Tab (under Main Screen) Displays list of posts for a given area.
 * 
 */
public class FeedFragment extends ListFragment {

    private PostsManager mPostsManager;
    public static final String postIdValue = "com.example.POSTIDVALUE";
    private View footerView;

    public FeedFragment() {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mPostsManager = new PostsManager(activity);

        footerView = new View(this.getActivity().getApplicationContext());
        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(0, 0);
        footerView.setLayoutParams(lp);
    }

    @Override
    public void onResume() {
        super.onResume();

        getListView().setDivider(null);
        getListView().setDividerHeight(0);

        getListView().addFooterView(footerView);

        loadFromLocal();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        if (position < 2) {
            footerView.setLayoutParams(new AbsListView.LayoutParams(0, 0)); // footer temporarily "removed" here (proof
                                                                            // of concept), but should be
                                                                            // removed upon closing "detail panel"
        } else {
            footerView.setLayoutParams(new AbsListView.LayoutParams(0, 1400)); // Arbitrary large height, but should be
                                                                               // "smarter" to get screen height
        }
        this.getListView().smoothScrollToPositionFromTop(position, 0, 250);

        // commented out the code below, so it won't open the post detail screen

        // TextView idView = (TextView) v.findViewById(R.id.row_id);
        //
        // long postId;
        //
        // try {
        // postId = Long.parseLong(idView.getText().toString());
        //
        // Intent intent = new Intent(getActivity(), PostDetailActivity.class);
        // intent.putExtra(postIdValue, postId);
        // startActivity(intent);
        //
        // } catch (NumberFormatException e) {
        // Toast.makeText(getActivity(), "Invalid Post", Toast.LENGTH_SHORT).show();
        // }
    }

    public void loadFromLocal() {
        Cursor postCursor = mPostsManager.getAllPostsCursor();

        String[] from = new String[] { PostsSQLiteHelper.COLUMN_ID, PostsSQLiteHelper.COLUMN_MESSAGE, PostsSQLiteHelper.COLUMN_LATITUDE,
                PostsSQLiteHelper.COLUMN_USER_NAME, PostsSQLiteHelper.COLUMN_LONGITUDE };
        int[] to = new int[] { R.id.row_id, R.id.row_message, R.id.row_latitude, R.id.row_username, R.id.row_longitude };

        ListAdapter adapter = new SimpleCursorAdapter(getActivity(), R.layout.list_feed_row, postCursor, from, to, 0);

        setListAdapter(adapter);
    }

    public void loadFromServer() {
        AsyncHttpClient client = new AsyncHttpClient();

        // POSTs to the specified URL with the entity, with text/plain as the content type
        client.get(Rove.SERVER_BASE_URL + "/posts?lat=44&lng=44&dist=200", new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(String response) {
                // this is the async callback
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Post post = new Post(jsonObject);

                        // TODO fill out the listview with these posts
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });
    }

}
