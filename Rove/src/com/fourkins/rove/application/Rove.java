package com.fourkins.rove.application;

import android.app.Application;

public class Rove extends Application {

    private String userName = null;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
