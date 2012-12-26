package com.fourkins.rove.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;

import com.fourkins.rove.R;

public class SplashScreenActivity extends Activity {

    protected int _splashTime = 5000;
    private Thread splashThread;

    // Trying to make a splash screen here
    @Override
    // wtf is a savedInstanceState
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Apparently need to use activity_main, want to use something else
        setContentView(R.layout.activity_splash_screen);

        splashThread = new Thread() {
            @Override
            public void run() {
                try {
                    synchronized (this) {
                        // 5s wait
                        wait(_splashTime);
                    }
                } catch (InterruptedException e) {
                    // do nothing
                } finally {
                    finish();
                    // NOTE: Splashscreen.this because current context is set to run() function
                    Intent myIntent = new Intent(SplashScreenActivity.this, com.fourkins.rove.activity.MainActivity.class);
                    startActivity(myIntent);

                }
            }
        };
        splashThread.start();
    }

    // Kill splashscreen on tap
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            synchronized (splashThread) {
                splashThread.notifyAll();
            }
        }
        return true;
    }
}
