package com.fourkins.rove.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class PostsSQLiteHelper extends SQLiteOpenHelper {

    public static final String TABLE_POSTS = "posts";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_USER_NAME = "user_name";
    public static final String COLUMN_LONGITUDE = "longitude";
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_MESSAGE = "message";
    public static final String COLUMN_TIMESTAMP = "timestamp";

    private static final String DATABASE_NAME = "rove.db";
    private static final int VERSION = 1;

    //@formatter:off
    // Database creation SQL statement
    private static final String CREATE_POSTS_TABLE = 
            "create table " + TABLE_POSTS + 
            "(" + 
                COLUMN_ID + " integer primary key autoincrement, " +
                COLUMN_USER_NAME + " text not null, " +
                COLUMN_LATITUDE + " real not null, " +
                COLUMN_LONGITUDE + " real not null, " +
                COLUMN_MESSAGE + " text not null, " +
                COLUMN_TIMESTAMP + " integer not null " +
            ");";    
    //@formatter:on

    public PostsSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_POSTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(PostsSQLiteHelper.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_POSTS);
        onCreate(db);
    }

}
