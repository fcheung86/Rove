package com.fourkins.rove.application;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * Helper Class to store Application Preferences
 * 
 * - User: current logged in user; also used to determine if login is required
 */
public class AppPreferences {
    private static final String APP_SHARED_PREFS = "com.fourkins.rove_preferences"; // Name of the file -.xml
    private SharedPreferences appSharedPrefs;
    private Editor prefsEditor;

    public AppPreferences(Context context) {
        this.appSharedPrefs = context.getSharedPreferences(APP_SHARED_PREFS, Activity.MODE_PRIVATE);
        this.prefsEditor = appSharedPrefs.edit();
    }

    public String getUser() {
        return appSharedPrefs.getString("user", "");
    }

    public void saveUser(String text) {
        prefsEditor.putString("user", text);
        prefsEditor.commit();
    }
}
