package com.fourkins.rove.sqlite.posts;

import java.util.List;
import java.util.Random;

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
        long latitude = post.getLatitude();
        long longitude = post.getLongitude();
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

    public void populateTable() {
        ds.open();

        ds.deleteAllPosts();

        Random rand = new Random();

        for (int i = 1; i <= 5; i++) {

            // use 35,35 as the limit for now, keep things in Africa
            double lat = rand.nextDouble() * 35.0 * (rand.nextInt(2) == 0 ? -1.0 : 1.0);
            double lng = rand.nextDouble() * 35.0 * (rand.nextInt(2) == 0 ? -1.0 : 1.0);

            ContentValues values = new ContentValues();
            values.put(PostsSQLiteHelper.COLUMN_USER_NAME, "user" + i);
            values.put(PostsSQLiteHelper.COLUMN_LATITUDE, lat);
            values.put(PostsSQLiteHelper.COLUMN_LONGITUDE, lng);
            values.put(PostsSQLiteHelper.COLUMN_MESSAGE, "message" + i);

            ds.insertPost(values);
        }

        ds.close();
    }
}
