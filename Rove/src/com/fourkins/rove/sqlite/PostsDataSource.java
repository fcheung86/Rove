package com.fourkins.rove.sqlite;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.fourkins.rove.sqlite.posts.Post;

public class PostsDataSource {

    // Database fields
    private SQLiteDatabase database;
    private PostsSQLiteHelper dbHelper;
    private String[] allColumns = { PostsSQLiteHelper.COLUMN_ID, PostsSQLiteHelper.COLUMN_USER_NAME, PostsSQLiteHelper.COLUMN_LATITUDE,
            PostsSQLiteHelper.COLUMN_LONGITUDE, PostsSQLiteHelper.COLUMN_MESSAGE };

    public PostsDataSource(Context context) {
        dbHelper = new PostsSQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void deleteAllPosts() {
        database.delete(PostsSQLiteHelper.TABLE_POSTS, null, null);
    }

    public void insertPost(ContentValues values) {
        database.insert(PostsSQLiteHelper.TABLE_POSTS, null, values);
    }

    public List<Post> getAllPosts() {
        List<Post> posts = new ArrayList<Post>();

        Cursor cursor = database.query(PostsSQLiteHelper.TABLE_POSTS, allColumns, null, null, null, null, null);

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            Post post = cursorToPost(cursor);
            posts.add(post);
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();

        return posts;
    }

    private Post cursorToPost(Cursor cursor) {
        Post post = new Post();

        post.setId(cursor.getLong(0));
        post.setUserName(cursor.getString(1));
        post.setLatitude(cursor.getLong(2));
        post.setLongitude(cursor.getLong(3));
        post.setMessage(cursor.getString(4));

        return post;
    }
}
