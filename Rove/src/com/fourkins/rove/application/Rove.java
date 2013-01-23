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
     * If you want to run a local server, use something like 192.168.x.x/RoveServer
     */
    public static final String SERVER_BASE_URL = "http://rove.elasticbeanstalk.com";

    private boolean flagFeedDetail = false;

    public boolean getFlagFeedDetail() {
        return flagFeedDetail;
    }

    public void setFlagFeedDetail(boolean FlagFeedDetail) {
        flagFeedDetail = FlagFeedDetail;
    }

    private String userName = null;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
