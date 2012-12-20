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

        if (ds.getAllPostsCount() == 0) {
            ds.deleteAllPosts();

            Random rand = new Random();

            for (int i = 1; i <= 5; i++) {

                // use 35,35 as the limit for now, keep things in Africa
                int lat = rand.nextInt(35) * (rand.nextInt(2) == 0 ? -1 : 1);
                int lng = rand.nextInt(35) * (rand.nextInt(2) == 0 ? -1 : 1);

                ContentValues values = new ContentValues();
                values.put(PostsSQLiteHelper.COLUMN_USER_NAME, "user" + i);
                values.put(PostsSQLiteHelper.COLUMN_LATITUDE, lat);
                values.put(PostsSQLiteHelper.COLUMN_LONGITUDE, lng);
                values.put(PostsSQLiteHelper.COLUMN_MESSAGE, "message" + i);

                ds.insertPost(values);
            }
        }

        ds.close();
    }
}
