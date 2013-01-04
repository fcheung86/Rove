package com.fourkins.rove.application;

import android.app.Application;

/**
 * 
 * Helper Class to store Global Variables for the application.
 * 
 */
public class Rove extends Application {

    private String userName = null;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
