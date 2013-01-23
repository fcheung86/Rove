package com.fourkins.rove.activity;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;

import com.fourkins.rove.R;
import com.fourkins.rove.application.AppPreferences;
import com.fourkins.rove.application.Rove;
import com.fourkins.rove.fragments.FeedFragment;
import com.fourkins.rove.fragments.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;

/**
 * Main Screen for the app.
 * 
 * Contains two tabs: - Feed: Displays lists of posts from other users, in a given area - Map: Displays the Map (powered
 * by Google Map) and plots "pins" for other users' posts
 * 
 * Also configures menu option: - Add: Add new posts for current location (shows on Action Bar) - Logout: Log out from
 * current user, redirects to login screen (sub-menu) - Settings (sub-menu)
 */
public class MainActivity extends FragmentActivity implements ActionBar.TabListener {

    /**
     * The serialization (saved instance state) Bundle key representing the current tab position.
     */
    private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";
    private AppPreferences mAppPrefs;

    private FeedFragment mFeedFragment;
    private SupportMapFragment mMapFragment;

    private Fragment mSelectedFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAppPrefs = new AppPreferences(getApplicationContext());

        setContentView(R.layout.activity_main);

        // Set up the action bar to show tabs.
        final ActionBar actionBar = getActionBar();

        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create instances of each fragment, and hide them
        mFeedFragment = new FeedFragment();
        mSelectedFragment = mFeedFragment;
        getSupportFragmentManager().beginTransaction().add(R.id.container, mFeedFragment).commit();

        /* commenting this out because it's causing a crash in simulator */
        mMapFragment = new MapFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.container, mMapFragment).hide(mMapFragment).commit();

        // For each of the sections in the app, add a tab to the action bar.
        // IMPORTANT: Must be done AFTER instantiating Fragments; these will trigger onTabSelected which references the
        // Fragments

        LinearLayout feed_detail = (LinearLayout) findViewById(R.id.detailmapinfo);
        feed_detail.setVisibility(View.GONE);

        actionBar.addTab(actionBar.newTab().setText(R.string.feed_section).setTabListener(this));
        actionBar.addTab(actionBar.newTab().setText(R.string.map_section).setTabListener(this));
    }

    protected void onStart() {
        super.onStart();

    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Restore the previously serialized current tab position.
        if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
            getActionBar().setSelectedNavigationItem(savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // Serialize the current tab position.
        outState.putInt(STATE_SELECTED_NAVIGATION_ITEM, getActionBar().getSelectedNavigationIndex());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.menu_add:
            Intent intent = new Intent(this, NewPostActivity.class);
            startActivity(intent);
            return true;

        case R.id.menu_logout: // Logout: Simply remove AppPreference "user", and redirect back to login screen
            mAppPrefs.saveUser("");
            Intent loginIntent = new Intent(MainActivity.this, com.fourkins.rove.activity.SplashScreenActivity.class);
            finish();
            startActivity(loginIntent);
            return true;

        case R.id.menu_load_local:
            if (mSelectedFragment instanceof FeedFragment) {
                ((FeedFragment) mSelectedFragment).loadFromLocal();
            } else if (mSelectedFragment instanceof MapFragment) {
                ((MapFragment) mSelectedFragment).loadFromLocal();
            }

            return true;

        case R.id.menu_load_server:
            if (mSelectedFragment instanceof FeedFragment) {
                ((FeedFragment) mSelectedFragment).loadFromServer();
            } else if (mSelectedFragment instanceof MapFragment) {
                ((MapFragment) mSelectedFragment).loadFromServer();
            }

            return true;

        default:
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, show the tab contents in the
        // container view.

        switch (tab.getPosition()) {
        case 0: // "Feed"
            RemoveDetailScreen();
            
            getSupportFragmentManager().beginTransaction().show(mFeedFragment).hide(mMapFragment).commit();

            mSelectedFragment = mFeedFragment;
            break;
        /*
         * Phong's Stuff getSupportFragmentManager().beginTransaction().show(mFeedFragment).commit();
         * 
         * mSelectedFragment = mFeedFragment; break;
         */
        case 1: // "Map"
            RemoveDetailScreen();
            
            getSupportFragmentManager().beginTransaction().show(mMapFragment).hide(mFeedFragment).commit();

            mSelectedFragment = mMapFragment;

            break;
        default:
            return;
        }

    }
    
    public void RemoveDetailScreen()
    {
        LinearLayout feed_detail = (LinearLayout) findViewById(R.id.detailmapinfo);
        Animation animation;
        Rove rove = (Rove) getApplication();
        
        if( rove.getFlagFeedDetail())
        {
            animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.4f, Animation.RELATIVE_TO_PARENT, 1.0f);
            animation.setDuration(500);
            animation.setFillAfter(true);
            feed_detail.startAnimation(animation);
            rove.setFlagFeedDetail(false);
            feed_detail.setVisibility(View.GONE);
        }
    }
    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }
}
