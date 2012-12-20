package com.fourkins.rove.sqlite.posts;

import java.util.List;

import android.content.ContentValues;
import android.content.Context;

import com.fourkins.rove.sqlite.PostsDataSource;
import com.fourkins.rove.sqlite.PostsSQLiteHelper;

public class PostsManager {

    private PostsDataSource ds;

    public PostsManager(Context context) {
        ds = new PostsDataSource(context);

    }

    public List<Post> getAllPosts() {
        ds.open();

        List<Post> posts = ds.getAllPosts();

        ds.close();

        return posts;
    }

    public void insertPost(Post post) {
        String user = post.getUserName();
        double latitude = post.getLatitude();
        double longitude = post.getLongitude();
        String message = post.getMessage();

        ds.open();

        ContentValues values = new ContentValues();
        values.put(PostsSQLiteHelper.COLUMN_USER_NAME, user);
        values.put(PostsSQLiteHelper.COLUMN_LATITUDE, latitude);
        values.put(PostsSQLiteHelper.COLUMN_LONGITUDE, longitude);
        values.put(PostsSQLiteHelper.COLUMN_MESSAGE, message);

        ds.insertPost(values);
        ds.close();
    }
}
