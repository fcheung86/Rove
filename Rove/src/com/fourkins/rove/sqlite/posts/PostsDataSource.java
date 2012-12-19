package com.fourkins.rove.sqlite.posts;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.fourkins.rove.sqlite.RoveSQLiteHelper;

public class PostsDataSource {

    private boolean isPopulated = false;

    // Database fields
    private SQLiteDatabase database;
    private RoveSQLiteHelper dbHelper;
    private String[] allColumns = { RoveSQLiteHelper.POSTS_ID, RoveSQLiteHelper.POSTS_USER_NAME, RoveSQLiteHelper.POSTS_LATITUDE,
            RoveSQLiteHelper.POSTS_LONGITUDE, RoveSQLiteHelper.POSTS_MESSAGE };

    public PostsDataSource(Context context) {
        dbHelper = new RoveSQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();

        if (!isPopulated) {
            // randomly populate table for now
            populateTable();
        }
    }

    public void close() {
        dbHelper.close();
    }

    public List<Post> getAllPosts() {
        List<Post> posts = new ArrayList<Post>();

        Cursor cursor = database.query(RoveSQLiteHelper.POSTS_TABLE, allColumns, null, null, null, null, null);

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

    private void populateTable() {
        database.delete(RoveSQLiteHelper.POSTS_TABLE, null, null);

        for (int i = 1; i <= 20; i++) {
            ContentValues values = new ContentValues();
            values.put(RoveSQLiteHelper.POSTS_USER_NAME, "user" + i);
            values.put(RoveSQLiteHelper.POSTS_LATITUDE, i * 10);
            values.put(RoveSQLiteHelper.POSTS_LONGITUDE, i * 100);
            values.put(RoveSQLiteHelper.POSTS_MESSAGE, "message" + i);

            database.insert(RoveSQLiteHelper.POSTS_TABLE, null, values);
        }

        isPopulated = true;
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
