package com.fourkins.rove.posts;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.location.Location;

import com.fourkins.rove.application.Rove;
import com.fourkins.rove.util.LocationUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class PostsManager {

    private PostsDataSource ds;
    private Context context;

    public PostsManager(Context context) {
        ds = new PostsDataSource(context);
        this.context = context;
    }

    public List<Post> getAllPosts() {
        ds.open();
        List<Post> posts = ds.getAllPosts();
        ds.close();

        return posts;
    }

    public void getPostsFromServer(AsyncHttpResponseHandler responseHandler) {
        AsyncHttpClient client = new AsyncHttpClient();

        final Location loc = LocationUtil.getInstance(context).getCurrentLocation();
        double lat = loc.getLatitude();
        double lng = loc.getLongitude();

        String url = Rove.SERVER_BASE_URL + "/posts?lat=" + lat + "&lng=" + lng + "&dist=200";

        // makes a GET request to the server
        client.get(url, responseHandler);
    }

    public List<Post> convertJsonToPosts(String json) {
        List<Post> posts = new ArrayList<Post>();

        try {
            JSONArray jsonArray = new JSONArray(json);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                Post post = new Post(jsonObject);
                posts.add(post);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return posts;
    }

    public Post getPost(long id) {
        ds.open();
        Post post = ds.getPost(id);
        ds.close();

        return post;
    }

    public void insertPost(Post post) {
        ds.open();
        ds.insertPost(post);
        ds.close();
    }

}
