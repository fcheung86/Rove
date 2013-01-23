package com.fourkins.rove.application;

import android.app.Application;

/**
 * 
 * Helper Class to store Global Variables for the application.
 * 
 */
public class Rove extends Application {

    /*
     * The base URL of the Rove Server
     * 
     * This specifies the location of the Rove Server.
     * 
     * If you want to run a local server, use something like 192.168.x.x:8080/RoveServer
     */
    public static final String SERVER_BASE_URL = "http://rove.elasticbeanstalk.com";
    // public static final String SERVER_BASE_URL = "http://192.168.x.x:8080/RoveServer";

    private boolean flagFeedDetail = false;
    private String username = null;
    private long userId;

    public boolean getFlagFeedDetail() {
        return flagFeedDetail;
    }

    public void setFlagFeedDetail(boolean FlagFeedDetail) {
        flagFeedDetail = FlagFeedDetail;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
