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
     * This specifies the location of the Rove Server, change the IP, 192.168.x.x, to wherever the Rove Server is
     * running
     * 
     * If the client and server is running in the same local network, the IP should most likely be 192.168.x.x too, run
     * 'ipconfig' to see what the IP is
     */
    public static final String BASE_URL = "http://192.168.x.x:8080/RoveServer";

    private String userName = null;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
