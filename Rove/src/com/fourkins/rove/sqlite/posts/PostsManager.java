package com.fourkins.rove.sqlite.posts;

import java.util.List;

import android.content.Context;
import android.database.Cursor;

import com.fourkins.rove.sqlite.PostsDataSource;

public class PostsManager {

    private PostsDataSource ds;

    public PostsManager(Context context) {
        ds = new PostsDataSource(context);

    }

    public Cursor getAllPostsCursor() {
        ds.open();
        Cursor cursor = ds.getAllPostsCursor();

        return cursor;
    }

    public List<Post> getAllPosts() {
        ds.open();
        List<Post> posts = ds.getAllPosts();
        ds.close();

        return posts;
    }

    public void insertPost(Post post) {
        ds.open();
        ds.insertPost(post);
        ds.close();
    }
}
