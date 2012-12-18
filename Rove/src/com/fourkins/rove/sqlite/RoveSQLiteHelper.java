package com.fourkins.rove.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class RoveSQLiteHelper extends SQLiteOpenHelper {

    public static final String POSTS_TABLE = "posts";
    public static final String POSTS_ID = "_id";
    public static final String POSTS_USER_NAME = "user_name";
    public static final String POSTS_LONGITUDE = "longitude";
    public static final String POSTS_LATITUDE = "latitude";
    public static final String POSTS_MESSAGE = "message";

    private static final String DATABASE_NAME = "rove.db";
    private static final int VERSION = 1;

    //@formatter:off
    // Database creation SQL statement
    private static final String CREATE_POSTS_TABLE = 
            "create table " + POSTS_TABLE + 
            "(" + POSTS_ID + " integer primary key autoincrement, " +
            POSTS_USER_NAME + " text not null, " +
            POSTS_LATITUDE + " real not null, " +
            POSTS_LONGITUDE + " real not null, " +
            POSTS_MESSAGE+ " text not null " +
            ");";
    
    //@formatter:on

    public RoveSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_POSTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(RoveSQLiteHelper.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + POSTS_TABLE);
        onCreate(db);
    }

}
