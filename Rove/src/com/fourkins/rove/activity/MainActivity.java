package com.fourkins.rove.activity;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.fourkins.rove.R;
import com.fourkins.rove.application.AppPreferences;
import com.fourkins.rove.fragments.FeedFragment;
import com.fourkins.rove.fragments.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener {

    /**
     * The serialization (saved instance state) Bundle key representing the current tab position.
     */
    private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";
    private AppPreferences mAppPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAppPrefs = new AppPreferences(getApplicationContext());
        Intent loginIntent = new Intent(this, LoginActivity.class);

        setContentView(R.layout.activity_main);

        // Set up the action bar to show tabs.
        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // For each of the sections in the app, add a tab to the action bar.
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
        case R.id.menu_logout:
            mAppPrefs.saveUser("");
            Intent loginIntent = new Intent(MainActivity.this, com.fourkins.rove.activity.LoginActivity.class);
            finish();
            startActivity(loginIntent);
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
        case 0:
            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.detailmapinfo);
            LinearLayout mapLayout = (LinearLayout) findViewById(R.id.emptyView);

            LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 2f);
            LayoutParams mapParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 0f);

            mapLayout.setLayoutParams(mapParams);
            linearLayout.setLayoutParams(params);

            Fragment fragment = new FeedFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
            break;
        case 1:
            SupportMapFragment mapFragment = new MapFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.container, mapFragment).commit();
            break;
        default:
            return;
        }

    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

}
