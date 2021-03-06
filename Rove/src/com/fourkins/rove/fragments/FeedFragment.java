package com.fourkins.rove.fragments;

import java.util.List;

import android.app.Activity;
import android.location.Location;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.fourkins.rove.R;
import com.fourkins.rove.application.Rove;
import com.fourkins.rove.posts.Post;
import com.fourkins.rove.posts.PostAdapter;
import com.fourkins.rove.posts.PostsManager;
import com.fourkins.rove.util.LocationUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * "Feed" Tab (under Main Screen) Displays list of posts for a given area.
 */
public class FeedFragment extends ListFragment {

    private PostsManager mPostsManager;
    public static final String postIdValue = "com.fourkins.rove.POSTIDVALUE";
    private View footerView;

    private LinearLayout mLinearLayout;
    private LinearLayout transparentView;
    private LinearLayout detailView;

    public FeedFragment() {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mPostsManager = new PostsManager(activity);

        mLinearLayout = (LinearLayout) getActivity().findViewById(R.id.detailmapinfo);
        transparentView = (LinearLayout) getActivity().findViewById(R.id.transparent_view);
        detailView = (LinearLayout) getActivity().findViewById(R.id.detailmapinfo_view);
        
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

        loadFromServer();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        int vHeight = this.getView().getMeasuredHeight();
        int itemHeight = v.getMeasuredHeight();

        float slideToRatio = ((float) itemHeight / vHeight);

        footerView.setLayoutParams(new AbsListView.LayoutParams(0, vHeight - 100)); // Arbitrary large height, but
                                                                                    // should
                                                                                    // be
                                                                                    // "smarter" to get screen height

        this.getListView().smoothScrollToPositionFromTop(position, 0, 250);

        ToggleFeedDetail(position, slideToRatio);
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

    public void ToggleFeedDetail(int position, float slideToRatio) {
        Animation animation;

        Rove rove = (Rove) getActivity().getApplication();
        final boolean flagFeedDetail = rove.getFlagFeedDetail();
        final int postDisplayPosition = rove.getPostDisplayPosition();
        int vHeight = this.getView().getMeasuredHeight();
        
        if (flagFeedDetail == false) {
            // if feed_detail at bottom, open
            
            transparentView.setVisibility(View.VISIBLE);
            transparentView.getLayoutParams().height = (int) (vHeight*slideToRatio);
            transparentView.requestLayout();
            detailView.getLayoutParams().height = (int) (vHeight*(1-slideToRatio));
            detailView.requestLayout();
            detailView.setClickable(true);
            
            animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT,
                    1.0f, Animation.RELATIVE_TO_PARENT, 0.0f);
            animation.setDuration(500);
            animation.setFillAfter(true);
            mLinearLayout.startAnimation(animation);
            rove.setFlagFeedDetail(true);
            mLinearLayout.setVisibility(View.VISIBLE);
            rove.setPostDisplayPosition(position);

        } else {
            // if at top, close
            if (postDisplayPosition == position) {
                animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f,
                        Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 1.0f);
                animation.setDuration(500);
                animation.setFillAfter(true);
                mLinearLayout.startAnimation(animation);
                rove.setFlagFeedDetail(false);
                mLinearLayout.setVisibility(View.GONE);
                detailView.setClickable(false);

                Thread scrollThread = new Thread() {
                    @Override
                    public void run() {
                        try {
                            synchronized (this) {
                                wait(500);
                                getActivity().runOnUiThread(new Runnable() {
                                    public void run() {
                                        footerView.setLayoutParams(new AbsListView.LayoutParams(0, 0));
                                    }
                                });

                            }
                        } catch (InterruptedException e) {
                            // do nothing
                        }
                    }
                };
                scrollThread.start();

            } else {
                rove.setPostDisplayPosition(position);
            }
        }
    }

    public void loadFromLocal() {

        Location currentLocation = LocationUtil.getInstance(getActivity()).getCurrentLocation();
        List<Post> posts = mPostsManager.getAllPosts();
        PostAdapter adapter = new PostAdapter(getActivity(), R.layout.feedfragment_listview_item, posts, currentLocation);
        setListAdapter(adapter);
    }

    public void loadFromServer() {
        mPostsManager.getPostsFromServer(new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(String response) {
                Location loc = LocationUtil.getInstance(getActivity()).getCurrentLocation();
                List<Post> posts = mPostsManager.convertJsonToPosts(response);

                if (getActivity() != null) {
                    PostAdapter adapter = new PostAdapter(getActivity(), R.layout.feedfragment_listview_item, posts, loc);
                    setListAdapter(adapter);
                }
            }

        });
    }
}
