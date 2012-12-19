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

        // TODO populate table here for now
        populateTable();
    }

    public List<Post> getAllPosts() {
        ds.open();

        List<Post> posts = ds.getAllPosts();

        ds.close();

        return posts;
    }

    private void populateTable() {
        ds.open();

        ds.deleteAllPosts();

        for (int i = 1; i <= 20; i++) {
            ContentValues values = new ContentValues();
            values.put(PostsSQLiteHelper.COLUMN_USER_NAME, "user" + i);
            values.put(PostsSQLiteHelper.COLUMN_LATITUDE, i * 10);
            values.put(PostsSQLiteHelper.COLUMN_LONGITUDE, i * 100);
            values.put(PostsSQLiteHelper.COLUMN_MESSAGE, "message" + i);

            ds.insertPost(values);
        }

        ds.close();
    }

}
