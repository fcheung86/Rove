package com.fourkins.rove.posts;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class PostsDataSource {

    // Database fields
    private SQLiteDatabase database;
    private PostsSQLiteHelper dbHelper;
    private String[] allColumns = { PostsSQLiteHelper.COLUMN_ID, PostsSQLiteHelper.COLUMN_USER_NAME, PostsSQLiteHelper.COLUMN_LATITUDE,
            PostsSQLiteHelper.COLUMN_LONGITUDE, PostsSQLiteHelper.COLUMN_MESSAGE, PostsSQLiteHelper.COLUMN_ADDRESS, PostsSQLiteHelper.COLUMN_CITY,
            PostsSQLiteHelper.COLUMN_TIMESTAMP };

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

    public void insertPost(Post post) {
        ContentValues values = new ContentValues();

        values.put(PostsSQLiteHelper.COLUMN_USER_NAME, post.getUsername());
        values.put(PostsSQLiteHelper.COLUMN_LATITUDE, post.getLatitude());
        values.put(PostsSQLiteHelper.COLUMN_LONGITUDE, post.getLongitude());
        values.put(PostsSQLiteHelper.COLUMN_MESSAGE, post.getMessage());
        values.put(PostsSQLiteHelper.COLUMN_ADDRESS, post.getAddress());
        values.put(PostsSQLiteHelper.COLUMN_CITY, post.getCity());
        values.put(PostsSQLiteHelper.COLUMN_TIMESTAMP, post.getTimestamp().getTime());

        database.insert(PostsSQLiteHelper.TABLE_POSTS, null, values);
    }

    public Cursor getAllPostsCursor() {
        return database.query(PostsSQLiteHelper.TABLE_POSTS, allColumns, null, null, null, null, PostsSQLiteHelper.COLUMN_TIMESTAMP + " DESC");
    }

    public List<Post> getAllPosts() {
        List<Post> posts = new ArrayList<Post>();

        Cursor cursor = getAllPostsCursor();

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

    public Post getPost(int id) {
        String where = PostsSQLiteHelper.COLUMN_ID + "=?";
        String whereArgs[] = new String[] { String.valueOf(id) };

        Cursor cursor = database.query(PostsSQLiteHelper.TABLE_POSTS, allColumns, where, whereArgs, null, null, null);

        cursor.moveToFirst();

        if (!cursor.isAfterLast()) {
            return cursorToPost(cursor);
        } else {
            return null;
        }
    }

    private Post cursorToPost(Cursor cursor) {
        Post post = new Post();

        post.setPostId(cursor.getInt(0));
        post.setUsername(cursor.getString(1));
        post.setLatitude(cursor.getDouble(2));
        post.setLongitude(cursor.getDouble(3));
        post.setMessage(cursor.getString(4));
        post.setAddress(cursor.getString(5));
        post.setCity(cursor.getString(6));
        post.setTimestamp(new Timestamp(cursor.getLong(7)));

        return post;
    }
}
