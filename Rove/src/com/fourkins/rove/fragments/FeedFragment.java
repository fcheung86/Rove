package com.fourkins.rove.fragments;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.fourkins.rove.R;
import com.fourkins.rove.sqlite.posts.Post;
import com.fourkins.rove.sqlite.posts.PostsManager;

public class FeedFragment extends Fragment {

    private ArrayAdapter<Post> adapter;

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

        View view = inflater.inflate(R.layout.activity_feed, container, false);

        /*
         * final Button button = (Button) view.findViewById(R.id.button_new); button.setOnClickListener(new
         * View.OnClickListener() { public void onClick(View v) { // Perform action on click } });
         */

        TextView t = (TextView) view.findViewById(R.id.textView1);
        t.setText("Some sample feeds");

        List<Post> posts = mPostsManager.getAllPosts();

        adapter = new ArrayAdapter<Post>(getActivity(), android.R.layout.simple_list_item_1, posts);

        ListView listView = (ListView) view.findViewById(R.id.feed);
        listView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        List<Post> posts = mPostsManager.getAllPosts();
        adapter.clear();
        adapter.addAll(posts);
        adapter.notifyDataSetChanged();
    }
}
